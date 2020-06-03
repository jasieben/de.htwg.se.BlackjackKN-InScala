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
import de.htwg.se.blackjackKN.gamelogic.util.UndoManager
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success, Try}

class Controller @Inject() extends ControllerInterface {
  val playerManagementServiceUrl = "http://localhost:1274/"

  val injector: Injector = Guice.createInjector(new BlackjackModule)
  var gameManager: GameManager = GameManager()
  var gameStates: List[GameState.Value] = List(GameState.IDLE)
  var revealed: Boolean = false
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
      gameStates = gameStates :+ GameState.ACE
      gameManager
    }
  }

  def startGame(): Unit = {
    gameManager = gameManager.generateDealerCards.addPlayerToGame()
  }

  def startNewRound(): Unit = {
    revealed = false

    gameManager = gameManager.clearDealerHand().clearPlayerHand(0)

    if (gameManager.getCardDeckSize <= 52) {
      gameStates = gameStates :+ GameState.SHUFFLING
      gameManager = gameManager.renewCardDeck()
    }
    for (_ <- 0 to 1) {
      gameManager = gameManager.addCardToPlayerHand(0, gameManager.drawCard()).dropCard()
      gameManager = gameManager.addCardToDealerHand(gameManager.drawCard()).dropCard()
    }

    gameStates = gameStates :+ GameState.FIRST_ROUND
    checkForAces()
    evaluate()
    notifyObservers()
  }

  def checkForAces(): Unit = {
    if (gameManager.containsCardTypeInPlayerHand(0, Ranks.Ace) != -1) {
      gameStates = gameStates :+ GameState.ACE
      if (gameManager.getPlayerCard(0, 0).rank == Ranks.Ace && gameManager.getPlayerCard(0, 1).rank == Ranks.Ace) {
        aceStrategy = new AceStrategy1
        gameManager = aceStrategy.execute()
      }
    }
  }

  def setBet(value: Int): Unit = {
    // Todo: set bet in Player Management

    val playerId = 123
    val json = Json.obj(
      "betValue" -> value
    ).toString()
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.POST, uri = playerManagementServiceUrl + s"player/$playerId/bet", entity = HttpEntity.apply(json)))
    val responseStringFuture = responseFuture.flatMap(r => Unmarshal(r.entity).to[String])
    val responseString = Await.result(responseStringFuture, Duration("10s"))

    val jsonResponse = Json.parse(responseString)
    val success = (jsonResponse \ "success").as[Boolean]
    if (success) {
      clearGameStates()
      gameStates = gameStates :+ GameState.BET_SET
      notifyObservers()
    } else {
      gameStates = gameStates :+ GameState.BET_FAILED
      notifyObservers()
    }
  }

  def stand(): Unit = {
    gameStates = gameStates :+ GameState.STAND
    revealDealer()
    evaluate()
  }

  def hit(): Unit = {
    gameManager = gameManager.addCardToPlayerHand(0, gameManager.drawCard()).dropCard()
    gameStates = gameStates :+ GameState.HIT
    checkForAces()
    evaluate()
  }

  def hitCommand(): Unit = {
    undoManager.doStep(new HitCommand(this))
    notifyObservers()
  }

  def standCommand(): Unit = {
    undoManager.doStep(new StandCommand(this))
    notifyObservers()
  }

  def undo(): Unit = {
    gameStates = gameStates :+ GameState.UNDO
    undoManager.undoStep()
    notifyObservers()
  }

  def redo(): Unit = {
    gameStates = gameStates :+ GameState.REDO
    undoManager.redoStep()
    notifyObservers()
  }

  def revealDealer(): Unit = {
    gameStates = gameStates :+ GameState.REVEAL
    if (gameManager.getDealerHandValue == 21 && gameManager.getDealerHandSize == 2) { //if dealer has bj as well
      gameStates = gameStates :+ GameState.DEALER_BLACKJACK
    } else {
      drawDealerCards()
    }
    revealed = true
  }

  private def drawDealerCards(): Unit = {
    gameStates = gameStates :+ GameState.DEALER_DRAWS
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
      gameStates = gameStates :+ GameState.PLAYER_BUST
      gameStates = gameStates :+ GameState.PLAYER_LOOSE
      evaluateBet(gameStates.last, 0)
      return
    } else if (gameManager.getDealerHandValue > 21) {
      gameStates = gameStates :+ GameState.DEALER_BUST
      gameStates = gameStates :+ GameState.PLAYER_WINS
      evaluateBet(gameStates.last, 0)
      return
    } else if (gameManager.getPlayerHandValue(0) == 21 && !revealed && gameManager.getPlayerHandSize(0) == 2) {
      gameStates = gameStates :+ GameState.PLAYER_BLACKJACK // if player has blackjack doesn't win yet
      revealDealer()
      // if player has blackjack and dealer hasn't pay out can continue
      if (gameStates.contains(GameState.PLAYER_BLACKJACK) && !gameStates.contains(GameState.DEALER_BLACKJACK)) {
        evaluate()
        return
      }
      // if not continue for push handling
    } else if (!revealed) { //when not revealed yet
      gameStates = gameStates :+ GameState.WAITING_FOR_INPUT
      return
    }

    //nobody busted
    if (gameManager.getDealerHandValue < 21 && gameManager.getPlayerHandValue(0) == 21) {
      gameStates = gameStates :+ GameState.PLAYER_WINS
      evaluateBet(gameStates.last, 0)
    } else if (gameManager.getDealerHandValue > gameManager.getPlayerHandValue(0)
      || (gameStates.contains(GameState.DEALER_BLACKJACK) && !gameStates.contains(GameState.PLAYER_BLACKJACK))) {
      gameStates = gameStates :+ GameState.PLAYER_LOOSE
      evaluateBet(gameStates.last, 0)
    } else if (gameManager.getDealerHandValue == gameManager.getPlayerHandValue(0)) {
      gameStates = gameStates :+ GameState.PUSH
      evaluateBet(gameStates.last, 0)
    } else if (gameManager.getDealerHandValue < gameManager.getPlayerHandValue(0)) {
      gameStates = gameStates :+ GameState.PLAYER_WINS
      evaluateBet(gameStates.last, 0)
    }
    gameStates = gameStates :+ GameState.IDLE
  }

  def evaluateBet(gameState: GameState, playerIndex: Int): Unit = {
    val json = Json.obj(
      "gameState" -> gameState.toString
    ).toString()
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.PUT, uri = playerManagementServiceUrl + s"player/$playerIndex/bet/resolve", entity = HttpEntity.apply(json)))
    responseFuture
      .onComplete {
        case Success(res) => println(res)
        case Failure(_) => sys.error("Could not resolve bet")
      }
  }

  def clearGameStates(): Unit = gameStates = List()
}
