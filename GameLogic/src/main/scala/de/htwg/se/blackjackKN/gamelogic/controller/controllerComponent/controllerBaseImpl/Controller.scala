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
  var currentPlayerIndex = 0

  implicit val actorSystem: ActorSystem = ActorSystem("actorSystemController")
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

  trait AceStrategy {
    def execute(): GameManager
  }

  class AceStrategy1 extends AceStrategy {
    override def execute(): GameManager = {
      val i: Int = gameManager.containsCardTypeInPlayerHand(currentPlayerIndex, Ranks.Ace)
      val oldCard = gameManager.getPlayerCard(currentPlayerIndex, i)
      val newValueAce = FaceCard(oldCard.suit, Ranks.Ace, isLowValueAce = true)
      gameManager.replaceCardInPlayerHand(currentPlayerIndex, i, newValueAce)
    }
  }

  class AceStrategy11 extends AceStrategy {
    override def execute(): GameManager = {
      gameManager = gameManager.pushGameState(GameState.ACE, currentPlayerIndex)
      gameManager
    }
  }

  def loadGameManager(playerId: String): Boolean = {
    val gameManagerOption = gameManagerPersistence.load(playerId)
    if (gameManagerOption.nonEmpty) {
      gameManager = gameManagerOption.get
      currentPlayerIndex = gameManager.currentPlayerInRound.indexOf(playerId)
      true
    } else {
      false
    }
  }

  def removePlayerFromGame(playerId: String): Unit = {
    gameManager = this.gameManager.removePlayerFromGame(playerId)
    this.gameManagerPersistence.update(this.gameManager)
  }

  def loadNewGameManager(playerId: String): Unit = {
    // Load a new Session
    val gameManagerOption = gameManagerPersistence.loadEmptySession()
    if (gameManagerOption.nonEmpty) {
      gameManager = gameManagerOption.get.addPlayerToGame(playerId)
      currentPlayerIndex = gameManager.currentPlayerInRound.indexOf(playerId)
      gameManagerPersistence.update(gameManager)
    } else {
      val newGameManager = GameManager().generateDealerCards.addPlayerToGame(playerId)
      gameManager = gameManagerPersistence.create(newGameManager)
      currentPlayerIndex = gameManager.currentPlayerInRound.indexOf(playerId)
    }
  }

  def startGame(): Unit = {
    gameManager = gameManager.generateDealerCards
  }

  def startNewRound(playerId: String, gameId: Option[String] = None): Unit = {
    if (gameManager.getCardDeckSize <= 52) {
      gameManager = gameManager.pushGameState(GameState.SHUFFLING, currentPlayerIndex)
      gameManager = gameManager.renewCardDeck()
    }
    for (_ <- 0 to 1) {
      gameManager = gameManager.addCardToPlayerHand(currentPlayerIndex, gameManager.drawCard()).dropCard()
      if (gameManager.currentPlayerInRound.indexOf(playerId) == 0) {
        gameManager = gameManager.addCardToDealerHand(gameManager.drawCard()).dropCard()
      }
    }

    gameManager = gameManager.pushGameState(GameState.FIRST_ROUND, currentPlayerIndex)
    checkForAces()
    evaluateImmediately(currentPlayerIndex)
    checkEvaluation()
    gameManager = gameManager.copy(isRunning = true)
    gameManagerPersistence.update(gameManager)
    notifyObservers()
  }

  def checkForAces(): Unit = {
    if (gameManager.containsCardTypeInPlayerHand(currentPlayerIndex, Ranks.Ace) != -1) {
      gameManager = gameManager.pushGameState(GameState.ACE, currentPlayerIndex)
      if (gameManager.getPlayerCard(currentPlayerIndex, 0).rank == Ranks.Ace && gameManager.getPlayerCard(currentPlayerIndex, 1).rank == Ranks.Ace) {
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
      gameManager = gameManager.pushGameState(GameState.BET_SET, currentPlayerIndex)
      notifyObservers()
    } else {
      gameManager = gameManager.pushGameState(GameState.BET_FAILED, currentPlayerIndex)
      this.removePlayerFromGame(playerId)
      notifyObservers()
    }
  }

  def stand(): Unit = {
    gameManager = gameManager.pushGameState(GameState.STAND, currentPlayerIndex)
    gameManager = gameManager.pushGameState(GameState.DONE, currentPlayerIndex)
    evaluateImmediately(currentPlayerIndex)
    checkEvaluation()
  }

  def hit(): Unit = {
    gameManager = gameManager.addCardToPlayerHand(currentPlayerIndex, gameManager.drawCard()).dropCard()
    gameManager = gameManager.pushGameState(GameState.HIT, currentPlayerIndex)
    checkForAces()
    evaluateImmediately(currentPlayerIndex)
    checkEvaluation()
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
    if (gameManager.revealed) {
      return
    }
    gameManager = gameManager.pushGameState(GameState.REVEAL, currentPlayerIndex)
    if (gameManager.getDealerHandValue == 21 && gameManager.getDealerHandSize == 2) { //if dealer has bj as well
      gameManager = gameManager.pushGameState(GameState.DEALER_BLACKJACK, currentPlayerIndex)
    } else {
      drawDealerCards()
    }
    gameManager = gameManager.copy(revealed = true)
  }

  private def drawDealerCards(): Unit = {
    if (gameManager.getDealerHandValue < 17) {
      gameManager = gameManager.pushGameState(GameState.DEALER_DRAWS, currentPlayerIndex)
    }

    while (gameManager.getDealerHandValue < 17) {
      gameManager = gameManager.addCardToDealerHand(gameManager.drawCard()).dropCard()
    }
  }

  def evaluateImmediately(playerIndex: Int): Boolean = {
    if (gameManager.containsCardTypeInPlayerHand(playerIndex, Ranks.Ace) != -1 && gameManager.getPlayerHandValue(playerIndex) != 21) {
      if (gameManager.getPlayerHandValue(playerIndex) > 21) {
        aceStrategy = new AceStrategy1
      } else {
        aceStrategy = new AceStrategy11
      }
      gameManager = aceStrategy.execute()
    }
    if (gameManager.getPlayerHandValue(playerIndex) > 21) {
      gameManager = gameManager.pushGameState(GameState.DONE, playerIndex)
      gameManager = gameManager.pushGameState(GameState.PLAYER_BUST, playerIndex)
      gameManager = gameManager.pushGameState(GameState.PLAYER_LOOSE, playerIndex)
      evaluateBet(gameManager.gameStates(playerIndex).last, playerIndex)
      true
    } else if (gameManager.getPlayerHandValue(playerIndex) == 21 && !gameManager.revealed) {
      gameManager = gameManager.pushGameState(GameState.DONE, playerIndex)
      gameManager = gameManager.pushGameState(GameState.PLAYER_BLACKJACK, playerIndex) // if player has blackjack doesn't win yet
      // if player has blackjack and dealer hasn't pay out can continue
      if (gameManager.gameStates(playerIndex).contains(GameState.PLAYER_BLACKJACK) && !gameManager.gameStates(playerIndex).contains(GameState.DEALER_BLACKJACK)) {

      }
      // if not continue for push handling
    }
    if (!gameManager.revealed) {
      //when not revealed yet
      gameManager = gameManager.pushGameState(GameState.WAITING_FOR_INPUT, playerIndex)
    }
    false
  }

  def evaluate(playerIndex: Int): Unit = {
    if (gameManager.gameStates(playerIndex).contains(GameState.PLAYER_LOOSE) ||
      gameManager.gameStates(playerIndex).contains(GameState.PLAYER_WINS)) {
      return
    }

    if (gameManager.getDealerHandValue > 21) {
      gameManager = gameManager.pushGameState(GameState.DONE, playerIndex)
      gameManager = gameManager.pushGameState(GameState.DEALER_BUST, playerIndex)
      gameManager = gameManager.pushGameState(GameState.PLAYER_WINS, playerIndex)
      evaluateBet(gameManager.gameStates(playerIndex).last, playerIndex)
      return
    }

    //nobody busted
    gameManager = gameManager.pushGameState(GameState.DONE, playerIndex)

    if (gameManager.getDealerHandValue < 21 && gameManager.getPlayerHandValue(playerIndex) == 21) {
      gameManager = gameManager.pushGameState(GameState.PLAYER_WINS, playerIndex)
      evaluateBet(gameManager.gameStates(playerIndex).last, playerIndex)
    } else if (gameManager.getDealerHandValue > gameManager.getPlayerHandValue(playerIndex)
      || (gameManager.gameStates(playerIndex).contains(GameState.DEALER_BLACKJACK) && !gameManager.gameStates(playerIndex).contains(GameState.PLAYER_BLACKJACK))) {
      gameManager = gameManager.pushGameState(GameState.PLAYER_LOOSE, playerIndex)
      evaluateBet(gameManager.gameStates(playerIndex).last, playerIndex)
    } else if (gameManager.getDealerHandValue == gameManager.getPlayerHandValue(playerIndex)) {
      gameManager = gameManager.pushGameState(GameState.PUSH, playerIndex)
      evaluateBet(gameManager.gameStates(playerIndex).last, playerIndex)
    } else if (gameManager.getDealerHandValue < gameManager.getPlayerHandValue(playerIndex)) {
      gameManager = gameManager.pushGameState(GameState.PLAYER_WINS, playerIndex)
      evaluateBet(gameManager.gameStates(playerIndex).last, playerIndex)
    }
    gameManager = gameManager.pushGameState(GameState.IDLE, playerIndex)
  }

  def evaluateBet(gameState: GameState, playerIndex: Int): Unit = {
    val json = Json.obj(
      "gameState" -> gameState.toString
    ).toString()
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.PUT, uri = playerManagementServiceUrl + s"player/${gameManager.currentPlayerInRound(playerIndex)}/bet/resolve", entity = HttpEntity.apply(json)))
    responseFuture
      .onComplete {
        case Success(res) =>
          println(res.entity)
        case Failure(_) => sys.error("Could not resolve bet")
      }
  }

  def checkEvaluation(): Unit = {
    if (checkPlayersForDoneState()) {
      revealDealer()

      for (playerIndex <- gameManager.currentPlayerInRound.indices) {
        evaluate(playerIndex)
      }

      if (gameManager.revealed) {
        gameManagerPersistence.deleteGameManager(gameManager)
      }
    }
  }

  def clearGameStates(): GameManager = gameManager.copy(gameStates = List())

  def checkPlayersForDoneState(): Boolean = {
    val result = gameManager.gameStates.filter(list => {
      !list.contains(GameState.DONE)
    })

    result.isEmpty
  }
}
