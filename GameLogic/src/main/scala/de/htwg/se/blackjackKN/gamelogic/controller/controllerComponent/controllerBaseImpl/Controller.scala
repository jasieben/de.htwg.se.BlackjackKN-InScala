package de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.controllerBaseImpl

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.google.inject.{Guice, Inject, Injector}
import de.htwg.se.blackjackKN.gamelogic.BlackjackModule
import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.GameState.GameState
import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.{ControllerInterface, GameState}
import de.htwg.se.blackjackKN.gamelogic.model.{GameManager, Ranks, Suits}
import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.cardsBaseImpl.FaceCard
import de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent.GameManagerPersistenceInterface
import de.htwg.se.blackjackKN.gamelogic.util.UndoManager
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success, Try}

class Controller @Inject() extends ControllerInterface {
  private val environmentPlayerManagementHost = sys.env.getOrElse("PLAYER_MANAGEMENT_HOST", "localhost:9002")
  val playerManagementServiceUrl = s"http://$environmentPlayerManagementHost/"

  val injector: Injector = Guice.createInjector(new BlackjackModule)
  val gameManagerPersistence: GameManagerPersistenceInterface = injector.getInstance(classOf[GameManagerPersistenceInterface])
  var gameManager: GameManager = GameManager(None)
  var aceStrategy: AceStrategy = new AceStrategy11
  val undoManager = new UndoManager

  implicit val actorSystem: ActorSystem = ActorSystem("actorSystemController")
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

  trait AceStrategy {
    def execute(): GameManager
  }

  class AceStrategy1 extends AceStrategy {
    override def execute(): GameManager = {
      val i: Int = gameManager.containsCardTypeInPlayerHand(0, Ranks.Ace)
      val oldCard = gameManager.getPlayerCard(0, i)
      val newValueAce = FaceCard(oldCard.suit, Ranks.Ace, isLowValueAce = true)
      gameManager.replaceCardInPlayerHand(0, i, newValueAce)
    }
  }

  class AceStrategy11 extends AceStrategy {
    override def execute(): GameManager = {
      gameManager = gameManager.pushGameState(GameState.ACE)
      gameManager
    }
  }

  def loadGameManager(playerId: String): Boolean = {
    val gameManagerOption = gameManagerPersistence.load(playerId)
    if (gameManagerOption.nonEmpty) {
      gameManager = gameManagerOption.get
      true
    } else {
      false
    }
  }

  def loadNewGameManager(playerId: String): Unit = {
    // Load a new Session
    // TODO: Check if player already has a game
    val gameManagerOption = gameManagerPersistence.loadEmptySession()
    if (gameManagerOption.nonEmpty) {
      gameManager = gameManagerOption.get.addPlayerToGame(playerId)
      gameManagerPersistence.update(gameManager)
    } else {
      val newGameManager = GameManager().generateDealerCards.addPlayerToGame(playerId)
      gameManager = gameManagerPersistence.create(newGameManager)
    }
  }

  def startGame(): Unit = {
    gameManager = gameManager.generateDealerCards
  }

  def startNewRound(playerId: String, gameId: Option[String] = None): Unit = {
    if (gameManager.getCardDeckSize <= 52) {
      gameManager = gameManager.pushGameState(GameState.SHUFFLING)
      gameManager = gameManager.renewCardDeck()
    }
    for (_ <- 0 to 1) {
      gameManager = gameManager.addCardToPlayerHand(0, gameManager.drawCard()).dropCard()
      gameManager = gameManager.addCardToDealerHand(gameManager.drawCard()).dropCard()
    }

    gameManager = gameManager.pushGameState(GameState.FIRST_ROUND)
    checkForAces()
    evaluate()
    gameManagerPersistence.update(gameManager)
    notifyObservers()
  }

  def checkForAces(): Unit = {
    if (gameManager.containsCardTypeInPlayerHand(0, Ranks.Ace) != -1) {
      gameManager = gameManager.pushGameState(GameState.ACE)
      if (gameManager.getPlayerCard(0, 0).rank == Ranks.Ace && gameManager.getPlayerCard(0, 1).rank == Ranks.Ace) {
        aceStrategy = new AceStrategy1
        gameManager = aceStrategy.execute()
      }
    }
  }

  def setBet(playerId: String, value: Int): Unit = {
    val json = Json.obj(
      "betValue" -> value
    ).toString()
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.POST, uri = playerManagementServiceUrl + s"player/$playerId/bet", entity = HttpEntity.apply(json)))
    val responseStringFuture = responseFuture.flatMap(r => Unmarshal(r.entity).to[String])
    val responseString = Await.result(responseStringFuture, Duration("10s"))

    val jsonResponse = Json.parse(responseString)
    val success = (jsonResponse \ "success").as[Boolean]
    if (success) {
      gameManager = gameManager.pushGameState(GameState.BET_SET)
      notifyObservers()
      startNewRound(playerId)
    } else {
      gameManager = gameManager.pushGameState(GameState.BET_FAILED)
      notifyObservers()
    }
  }

  def stand(): Unit = {
    gameManager = gameManager.pushGameState(GameState.STAND)
    revealDealer()
    evaluate()
  }

  def hit(): Unit = {
    gameManager = gameManager.addCardToPlayerHand(0, gameManager.drawCard()).dropCard()
    gameManager = gameManager.pushGameState(GameState.HIT)
    checkForAces()
    evaluate()
  }

  def hitCommand(playerId: String): Boolean = {
    undoManager.doStep(new HitCommand(this))
    this.gameManagerPersistence.update(gameManager)
    notifyObservers()
    true
  }

  def standCommand(playerId: String): Boolean = {
    undoManager.doStep(new StandCommand(this))
    this.gameManagerPersistence.update(gameManager)
    notifyObservers()
    true
  }

  def undo(): Unit = {
    undoManager.undoStep()
    notifyObservers()
  }

  def redo(): Unit = {
    undoManager.redoStep()
    notifyObservers()
  }

  def revealDealer(): Unit = {
    gameManager = gameManager.pushGameState(GameState.REVEAL)
    if (gameManager.getDealerHandValue == 21 && gameManager.getDealerHandSize == 2) { //if dealer has bj as well
      gameManager = gameManager.pushGameState(GameState.DEALER_BLACKJACK)
    } else {
      drawDealerCards()
    }
    gameManager = gameManager.copy(revealed = true)
  }

  private def drawDealerCards(): Unit = {
    gameManager = gameManager.pushGameState(GameState.DEALER_DRAWS)
    while (gameManager.getDealerHandValue < 17) {
      gameManager = gameManager.addCardToDealerHand(gameManager.drawCard()).dropCard()
    }
  }

  def evaluate(): Unit = {
    if (gameManager.containsCardTypeInPlayerHand(0, Ranks.Ace) != -1 && gameManager.getPlayerHandValue(0) != 21) {
      if (gameManager.getPlayerHandValue(0) > 21) {
        aceStrategy = new AceStrategy1
      } else {
        aceStrategy = new AceStrategy11

      }
      gameManager = aceStrategy.execute()
    }
    if (gameManager.getPlayerHandValue(0) > 21) {
      gameManager = gameManager.pushGameState(GameState.PLAYER_BUST)
      gameManager = gameManager.pushGameState(GameState.PLAYER_LOOSE)
      evaluateBet(gameManager.gameStates.last, 0)
      return
    } else if (gameManager.getDealerHandValue > 21) {
      gameManager = gameManager.pushGameState(GameState.DEALER_BUST)
      gameManager = gameManager.pushGameState(GameState.PLAYER_WINS)
      evaluateBet(gameManager.gameStates.last, 0)
      return
    } else if (gameManager.getPlayerHandValue(0) == 21 && !gameManager.revealed && gameManager.getPlayerHandSize(0) == 2) {
      gameManager = gameManager.pushGameState(GameState.PLAYER_BLACKJACK) // if player has blackjack doesn't win yet
      revealDealer()
      // if player has blackjack and dealer hasn't pay out can continue
      if (gameManager.gameStates.contains(GameState.PLAYER_BLACKJACK) && !gameManager.gameStates.contains(GameState.DEALER_BLACKJACK)) {
        evaluate()
        return
      }
      // if not continue for push handling
    } else if (!gameManager.revealed) { //when not revealed yet
      gameManager = gameManager.pushGameState(GameState.WAITING_FOR_INPUT)
      return
    }

    //nobody busted
    if (gameManager.getDealerHandValue < 21 && gameManager.getPlayerHandValue(0) == 21) {
      gameManager = gameManager.pushGameState(GameState.PLAYER_WINS)
      evaluateBet(gameManager.gameStates.last, 0)
    } else if (gameManager.getDealerHandValue > gameManager.getPlayerHandValue(0)
      || (gameManager.gameStates.contains(GameState.DEALER_BLACKJACK) && !gameManager.gameStates.contains(GameState.PLAYER_BLACKJACK))) {
      gameManager = gameManager.pushGameState(GameState.PLAYER_LOOSE)
      evaluateBet(gameManager.gameStates.last, 0)
    } else if (gameManager.getDealerHandValue == gameManager.getPlayerHandValue(0)) {
      gameManager = gameManager.pushGameState(GameState.PUSH)
      evaluateBet(gameManager.gameStates.last, 0)
    } else if (gameManager.getDealerHandValue < gameManager.getPlayerHandValue(0)) {
      gameManager = gameManager.pushGameState(GameState.PLAYER_WINS)
      evaluateBet(gameManager.gameStates.last, 0)
    }
    gameManager = gameManager.pushGameState(GameState.IDLE)
  }

  def evaluateBet(gameState: GameState, playerIndex: Int): Unit = {
    val json = Json.obj(
      "gameState" -> gameState.toString
    ).toString()
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.PUT, uri = playerManagementServiceUrl + s"player/$playerIndex/bet/resolve", entity = HttpEntity.apply(json)))
    responseFuture
      .onComplete {
        case Success(res) =>
          println(res.entity)
          var finishedGameManager = clearGameStates().removePlayerFromGame(gameManager.currentPlayerInRound)
            .copy(revealed = false).clearDealerHand().clearPlayerHand(0)

          gameManagerPersistence.update(finishedGameManager)
        case Failure(_) => sys.error("Could not resolve bet")
      }
  }

  def clearGameStates(): GameManager = gameManager.copy(gameStates = List[GameState]())
}
