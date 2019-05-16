package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.model.{Dealer, Player}
import de.htwg.se.blackjackKN.util.Observable

class Controller extends Observable {
  val dealer = Dealer()
  val player = Player("Test")
  var gameStates : List[GameState.Value] = List(GameState.IDLE)
  var revealed : Boolean = false

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
    evaluate()
    notifyObservers
  }

  def stand() : Unit = {
    gameStates = gameStates :+ GameState.STAND
    revealDealer()
    notifyObservers
  }
  def hit() : Unit = {
    gameStates = gameStates :+ GameState.HIT
    player.addCardToHand(dealer.drawCard())
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
    // evaluation result doesn't matter
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
