package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.model.{Dealer, Player}
import de.htwg.se.blackjackKN.util.Observable

class Controller extends Observable {
  val dealer = Dealer()
  val player = Player("Test")
  var output : String = ""

  def startGame() : Unit = {
    dealer.generateDealerCards

  }
  def startNewRound() : Unit = {
    player.addCardToHand(dealer.drawCard())
    player.addCardToHand(dealer.drawCard())
    dealer.addCardToDealerHand(dealer.drawCard())
    dealer.addCardToDealerHand(dealer.drawCard())
    output = "You have a " + player.getCard(0) + " and a " + player.getCard(1)
    notifyObservers
  }
  def display : String = output
  def stand() : String = ""
  def hit() : String = ""
  def evaluate() : String = ""
}
