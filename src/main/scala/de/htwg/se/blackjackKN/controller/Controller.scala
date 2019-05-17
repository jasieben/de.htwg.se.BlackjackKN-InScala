package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.model.{Bet, Dealer, FaceCard, Player, Ranks}
import de.htwg.se.blackjackKN.util.Observable


class Controller extends Observable {
  val dealer = Dealer()
  val player = Player("Test")
  var gameStates : List[GameState.Value] = List(GameState.IDLE)
  var revealed : Boolean = false
  var aceStrategy : AceStrategy = new AceStrategy11

  trait AceStrategy {
    def execute
  }
  class AceStrategy1 extends AceStrategy {
    override def execute = {
      val i : Int = player.containsCardType(Ranks.Ace)
      player.getCard(i).value = 1
    }
  }
  class AceStrategy11 extends AceStrategy {
    override def execute = gameStates = gameStates :+ GameState.ACE
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
    if (player.containsCardType(Ranks.Ace) != -1) {
      gameStates = gameStates :+ GameState.ACE
    }
    evaluate()
    notifyObservers
  }
  def setBet(value : Int): Unit = {
    if (player.addBet(Bet(value))) gameStates = gameStates :+ GameState.BET_SET
    else gameStates = gameStates :+ GameState.BET_FAILED
    notifyObservers
  }

  def stand() : Unit = {
    gameStates = gameStates :+ GameState.STAND
    revealDealer()
    notifyObservers
  }
  def hit() : Unit = {
    player.addCardToHand(dealer.drawCard())
    gameStates = gameStates :+ GameState.HIT
    if (player.containsCardType(Ranks.Ace) != -1) {
      gameStates = gameStates :+ GameState.ACE
    }
    evaluate()
    notifyObservers
  }
  def revealDealer() : Unit = {
    gameStates = gameStates :+ GameState.REVEAL
    drawDealerCards()
    revealed = true
    evaluate()
  }
  private def drawDealerCards() : Unit = {
    gameStates = gameStates :+ GameState.DEALER_DRAWS
    while (dealer.getHandValue < 17) {
      dealer.addCardToHand(dealer.drawCard())
    }
  }
  def evaluate() : Unit = {
    if (player.containsCardType(Ranks.Ace) != -1) {
      if (player.getHandValue > 21) {
        aceStrategy = new AceStrategy1
        aceStrategy.execute
      } else {
        aceStrategy = new AceStrategy11
      }
    }
    if (player.getHandValue > 21) {
      gameStates = gameStates :+ GameState.PLAYER_BUST
      return
    } else if (dealer.getHandValue > 21) {
      gameStates = gameStates :+ GameState.DEALER_BUST
      return
    } else if (player.getHandValue == 21 && !revealed) {
      gameStates = gameStates :+ GameState.PLAYER_BLACKJACK
      revealDealer()
      return
    } else if (!revealed){  //when revealed
      gameStates = gameStates :+ GameState.WAITING_FOR_INPUT
      return
    }

    //nobody busted
    if (dealer.getHandValue < player.getHandValue) {
      gameStates = gameStates :+ GameState.PLAYER_WINS
    } else if (dealer.getHandValue > player.getHandValue) {
      gameStates = gameStates :+ GameState.PLAYER_LOOSE
    } else if (dealer.getHandValue == player.getHandValue) {
      gameStates = gameStates :+ GameState.PUSH
    }
    gameStates = gameStates :+ GameState.IDLE
  }

  def clearGameStates() : Unit = gameStates = List()
}
