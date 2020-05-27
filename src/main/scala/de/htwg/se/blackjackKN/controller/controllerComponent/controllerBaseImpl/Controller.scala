package de.htwg.se.blackjackKN.controller.controllerComponent.controllerBaseImpl

import com.google.inject.{Guice, Inject, Injector}
import de.htwg.se.blackjackKN.BlackjackModule
import de.htwg.se.blackjackKN.controller.controllerComponent.{ControllerInterface, GameState}
import de.htwg.se.blackjackKN.model.{Dealer, Ranks, Suits}
import de.htwg.se.blackjackKN.model.betComponent.Bet
import de.htwg.se.blackjackKN.model.cardsComponent.CardInterface
import de.htwg.se.blackjackKN.model.cardsComponent.cardsBaseImpl.FaceCard
import de.htwg.se.blackjackKN.model.fileioComponent.FileIOInterface
import de.htwg.se.blackjackKN.util.UndoManager


class Controller @Inject() extends ControllerInterface {
  val injector: Injector = Guice.createInjector(new BlackjackModule)
  var dealer: Dealer = Dealer()
  var player: Player = Player()
  var fileIO: FileIOInterface = injector.getInstance(classOf[FileIOInterface])
  var gameStates: List[GameState.Value] = List(GameState.IDLE)
  var revealed: Boolean = false
  var aceStrategy: AceStrategy = new AceStrategy11
  val undoManager = new UndoManager

  val win = new WinningHandler(None)
  val loose = new LoosingHandler(Option(win))
  val blackjack = new BlackjackHandler(Option(loose))
  val push = new PushHandler(Option(blackjack))

  trait AceStrategy {
    def execute(): Player
  }

  class AceStrategy1 extends AceStrategy {
    override def execute(): Player = {
      val i: Int = player.containsCardType(Ranks.Ace)
      val oldCard = player.getCard(i)
      val newValueAce = FaceCard(oldCard.suit, Ranks.Ace, isLowValueAce = true)
      player.replaceCardInHand(i, newValueAce)
    }
  }

  class AceStrategy11 extends AceStrategy {
    override def execute(): Player = {
      gameStates = gameStates :+ GameState.ACE
      player
    }
  }

  def createNewPlayer(name: String): Unit = {
    player = Player(name = name)
    fileIO.store(this.player)
    gameStates = gameStates :+ GameState.NEW_NAME
    notifyObservers()
  }

  def changePlayer(name: String): Unit = {
    val playerOption = fileIO.load(name)
    this.player = playerOption.getOrElse(this.player)
  }

  def startGame(): Unit = {
    dealer = dealer.generateDealerCards
  }

  def startNewRound(): Unit = {
    revealed = false
    player = player.clearHand()
    dealer = dealer.clearHand()

    if (dealer.getCardDeckSize <= 52) {
      gameStates = gameStates :+ GameState.SHUFFLING
      dealer = dealer.renewCardDeck()
    }
    for (_ <- 0 to 1) {
      player = player.addCardToHand(dealer.drawCard())
      dealer = dealer.dropCard()
      dealer = dealer.addCardToHand(dealer.drawCard())
      dealer = dealer.dropCard()
    }

    gameStates = gameStates :+ GameState.FIRST_ROUND
    checkForAces()
    evaluate()
    notifyObservers()
  }

  def checkForAces(): Unit = {
    if (player.containsCardType(Ranks.Ace) != -1) {
      gameStates = gameStates :+ GameState.ACE
      if (player.getCard(0).rank == Ranks.Ace && player.getCard(1).rank == Ranks.Ace) {
        aceStrategy = new AceStrategy1
        player = aceStrategy.execute()
      }
    }
  }

  def setBet(value: Int): Boolean = {
    player = player.newBet(value)

    player.bet match {
      case Some(_) =>
        clearGameStates()
        gameStates = gameStates :+ GameState.BET_SET
        notifyObservers()
        true
      case None =>
        gameStates = gameStates :+ GameState.BET_FAILED
        notifyObservers()
        false
    }
  }

  def stand(): Unit = {
    gameStates = gameStates :+ GameState.STAND
    revealDealer()
    evaluate()
  }

  def hit(): Unit = {
    player = player.addCardToHand(dealer.drawCard())
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
    if (dealer.getHandValue == 21 && dealer.getHandSize == 2) { //if dealer has bj as well
      gameStates = gameStates :+ GameState.DEALER_BLACKJACK
    } else {
      drawDealerCards()
    }
    revealed = true
  }

  private def drawDealerCards(): Unit = {
    gameStates = gameStates :+ GameState.DEALER_DRAWS
    while (dealer.getHandValue < 17) {
      dealer = dealer.addCardToHand(dealer.drawCard())
      dealer = dealer.dropCard()
    }
  }

  def evaluate(): Unit = {
    if (player.containsCardType(Ranks.Ace) != -1 && player.getHandValue != 21) {
      if (player.getHandValue > 21) {
        aceStrategy = new AceStrategy1
      } else {
        aceStrategy = new AceStrategy11

      }
      player = aceStrategy.execute()
    }
    if (player.getHandValue > 21) {
      gameStates = gameStates :+ GameState.PLAYER_BUST
      gameStates = gameStates :+ GameState.PLAYER_LOOSE
      player = push.handleRequest(GameState.PLAYER_LOOSE, this.player)
      fileIO.store(this.player)
      return
    } else if (dealer.getHandValue > 21) {
      gameStates = gameStates :+ GameState.DEALER_BUST
      gameStates = gameStates :+ GameState.PLAYER_WINS
      player = push.handleRequest(GameState.PLAYER_WINS, this.player)
      fileIO.store(this.player)
      return
    } else if (player.getHandValue == 21 && !revealed && player.getHandSize == 2) {
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
    if (dealer.getHandValue < 21 && player.getHandValue == 21) {
      gameStates = gameStates :+ GameState.PLAYER_WINS
      player = push.handleRequest(GameState.PLAYER_BLACKJACK, this.player)
      fileIO.store(this.player)
    } else if (dealer.getHandValue > player.getHandValue
      || (gameStates.contains(GameState.DEALER_BLACKJACK) && !gameStates.contains(GameState.PLAYER_BLACKJACK))) {
      gameStates = gameStates :+ GameState.PLAYER_LOOSE
      player = push.handleRequest(gameStates.last, this.player)
      fileIO.store(this.player)
    } else if (dealer.getHandValue == player.getHandValue) {
      gameStates = gameStates :+ GameState.PUSH
      player = push.handleRequest(gameStates.last, this.player)
      fileIO.store(this.player)
    } else if (dealer.getHandValue < player.getHandValue) {
      gameStates = gameStates :+ GameState.PLAYER_WINS
      player = push.handleRequest(gameStates.last, this.player)
      fileIO.store(this.player)
    }
    gameStates = gameStates :+ GameState.IDLE
  }

  def clearGameStates(): Unit = gameStates = List()
}
