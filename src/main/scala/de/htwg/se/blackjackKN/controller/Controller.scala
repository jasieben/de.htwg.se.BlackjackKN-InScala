package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.model.betComponent.Bet
import de.htwg.se.blackjackKN.model.personsComponent.personsBaseImpl.{Dealer, Player}
import de.htwg.se.blackjackKN.model.{Player, Ranks}
import de.htwg.se.blackjackKN.util.{Observable, UndoManager}


class Controller extends Observable {
  var dealer = Dealer()
  var player = Player()
  var gameStates : List[GameState.Value] = List(GameState.IDLE)
  var revealed : Boolean = false
  var aceStrategy : AceStrategy = new AceStrategy11
  val undoManager = new UndoManager

  val win = new WinningHandler(null)
  val loose = new LoosingHandler(win)
  val blackjack = new BlackjackHandler(loose)
  val push = new PushHandler(blackjack)

  trait AceStrategy {
    def execute()
  }
  class AceStrategy1 extends AceStrategy {
    override def execute() : Unit = {
      val i : Int = player.containsCardType(Ranks.Ace)
      player.getCard(i).value = 1
    }
  }

  class AceStrategy11 extends AceStrategy {
    override def execute() : Unit = gameStates = gameStates :+ GameState.ACE
  }
  def createNewPlayer(name: String) : Unit = {
    player = Player(name)
    gameStates = gameStates :+ GameState.NEW_NAME
    notifyObservers()
  }
  def startGame() : Unit = {
    dealer.generateDealerCards

  }
  def startNewRound() : Unit = {
    revealed = false
    player.clearHand()
    dealer.clearHand()
    if (dealer.getCardDeckSize <= 52) {
      gameStates = gameStates :+ GameState.SHUFFLING
      dealer.renewCardDeck()
    }

    player.addCardToHand(dealer.drawCard())
    dealer.addCardToHand(dealer.drawCard())
    player.addCardToHand(dealer.drawCard())
    dealer.addCardToHand(dealer.drawCard())
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
        aceStrategy.execute()
      }
    }
  }

  def setBet(value : Int): Boolean = {
    if (player.addBet(Bet(value))) {
      clearGameStates()
      gameStates = gameStates :+ GameState.BET_SET
      notifyObservers()
      true
    }
    else {
      gameStates = gameStates :+ GameState.BET_FAILED
      notifyObservers()
      false
    }

  }

  def stand() : Unit = {
    gameStates = gameStates :+ GameState.STAND
    revealDealer()
    evaluate()
  }
  def hit() : Unit = {
    player.addCardToHand(dealer.drawCard())
    gameStates = gameStates :+ GameState.HIT
    checkForAces()
    evaluate()
  }

  def hitCommand() : Unit = {
    undoManager.doStep(new HitCommand(this))
    notifyObservers()
  }

  def standCommand() : Unit = {
    undoManager.doStep(new StandCommand(this))
    notifyObservers()
  }

  def undo() : Unit = {
    gameStates = gameStates :+ GameState.UNDO
    undoManager.undoStep
    notifyObservers()
  }

  def redo() : Unit = {
    gameStates = gameStates :+ GameState.REDO
    undoManager.redoStep
    notifyObservers()
  }

  def revealDealer() : Unit = {
    gameStates = gameStates :+ GameState.REVEAL
    if (dealer.getHandValue == 21 && dealer.getHandSize == 2) { //if dealer has bj as well
      gameStates = gameStates :+ GameState.DEALER_BLACKJACK
    } else {
      drawDealerCards()
    }
    revealed = true
  }
  private def drawDealerCards() : Unit = {
    gameStates = gameStates :+ GameState.DEALER_DRAWS
    while (dealer.getHandValue < 17) {
      dealer.addCardToHand(dealer.drawCard())
    }
  }
  def evaluate() : Unit = {
    if (player.containsCardType(Ranks.Ace) != -1 && player.getHandValue != 21) {
      if (player.getHandValue > 21) {
        aceStrategy = new AceStrategy1
        aceStrategy.execute()
      } else {
        aceStrategy = new AceStrategy11
      }
    }
    if (player.getHandValue > 21) {
      gameStates = gameStates :+ GameState.PLAYER_BUST
      gameStates = gameStates :+ GameState.PLAYER_LOOSE
      push.handleRequest(GameState.PLAYER_LOOSE, this.player)
      return
    } else if (dealer.getHandValue > 21) {
      gameStates = gameStates :+ GameState.DEALER_BUST
      gameStates = gameStates :+ GameState.PLAYER_WINS
      push.handleRequest(GameState.PLAYER_WINS, this.player)
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
    } else if (!revealed){  //when not revealed yet
      gameStates = gameStates :+ GameState.WAITING_FOR_INPUT
      return
    }

    //nobody busted
    if (dealer.getHandValue < 21 && player.getHandValue == 21) {
      gameStates = gameStates :+ GameState.PLAYER_WINS
      push.handleRequest(GameState.PLAYER_BLACKJACK, this.player)
    } else if (dealer.getHandValue > player.getHandValue
      || (gameStates.contains(GameState.DEALER_BLACKJACK) && !gameStates.contains(GameState.PLAYER_BLACKJACK))) {
      gameStates = gameStates :+ GameState.PLAYER_LOOSE
      push.handleRequest(gameStates.last, this.player)
    } else if (dealer.getHandValue == player.getHandValue) {
      gameStates = gameStates :+ GameState.PUSH
      push.handleRequest(gameStates.last, this.player)
    } else if (dealer.getHandValue < player.getHandValue) {
      gameStates = gameStates :+ GameState.PLAYER_WINS
      push.handleRequest(gameStates.last, this.player)
    }
    gameStates = gameStates :+ GameState.IDLE
  }

  def clearGameStates() : Unit = gameStates = List()
}
