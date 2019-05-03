package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.model.{Dealer, Player}
import de.htwg.se.blackjackKN.util.Observable

class Controller extends Observable {
  val dealer = Dealer()
  val player = Player("Test")
  var output : String = ""
  var revealed : Boolean = false

  def startGame() : Unit = {
    dealer.generateDealerCards
    output = "\n.------..------..------..------..------..------..------..------..------.\n|B.--. ||L.--. ||A.--. |" +
      "|C.--. ||K.--. ||J.--. ||A.--. ||C.--. ||K.--. |\n| :(): || :/\\: || (\\/) || :/\\: || :/\\: || :(): || (\\" +
      "/) || :/\\: || :/\\: |\n| ()() || (__) || :\\/: || :\\/: || :\\/: || ()() || :\\/: || :\\/: || :\\/: |\n| '" +
      "--'B|| '--'L|| '--'A|| '--'C|| '--'K|| '--'J|| '--'A|| '--'C|| '--'K|\n`------'`------'`------'`------'`---" +
      "---'`------'`------'`------'`------'"
    output += "\nPress n to start a new game!"
    notifyObservers
  }
  def startNewRound() : Unit = {
    revealed = false
    player.clearHand()
    dealer.clearHand()
    player.addCardToHand(dealer.drawCard())
    dealer.addCardToHand(dealer.drawCard())
    player.addCardToHand(dealer.drawCard())
    dealer.addCardToHand(dealer.drawCard())
    output = "You have a " + player.getCard(0)
    output += "\nThe dealer has a " + dealer.getCard(0)
    output += "\nYou also have a " + player.getCard(1)
    output += "\nThe combined value of your cards is " + player.getHandValue
    evaluate()
    notifyObservers
  }
  def display : String = output
  def stand() : Unit = {
    output = "You stand"
    revealDealer()
    notifyObservers
  }
  def hit() : Unit = {
    if (player.getHandValue > 21) { //aka Player already busted
      output = "You cannot hit!"
      notifyObservers
      return
    }
    output = "You hit and draw a " +  player.addCardToHand(dealer.drawCard())
    output += "\nThe combined value of your cards is " + player.getHandValue
    evaluate()
    notifyObservers
  }
  def revealDealer() : Unit = {
    output += "\nThe dealer has a " + dealer.getCard(1)
    while (dealer.getHandValue < 17) {
      output += "\nThe dealer draws a " + dealer.addCardToHand(dealer.drawCard())
    }
    output += "\nThe dealers combined value of cards is " + dealer.getHandValue
    revealed = true
    evaluate()
  }
  def evaluate() : Unit = {
    // evaluation result doesn't matter
    if (player.getHandValue > 21) {
      output += "\nYou bust!"
      return
    } else if (dealer.getHandValue > 21) {
      output += "\nThe dealer busts, you win!"
      return
    } else if (player.getHandValue == 21 && !revealed) {
      output += "\nYou have a blackjack!"
      revealDealer()
      return
    } else if (!revealed){  //when revealed
      output += "\nWould you like to hit(h) or stand(s)?"
      return
    }

    //nobody busted
    if (dealer.getHandValue < player.getHandValue) {
      output += "\nYou win!"
    } else if (dealer.getHandValue > player.getHandValue) {
      output += "\nYou loose!"
    } else if (dealer.getHandValue == player.getHandValue) {
      output += "\nPush! You and the dealer have a combined card value of " + dealer.getHandValue
    }
  }
}
