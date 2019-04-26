package de.htwg.se.blackjackKN.aview

import de.htwg.se.blackjackKN.model.{Card, CardDeck, Dealer}

import scala.collection.mutable.ListBuffer

class Tui {
  val dealer = Dealer()
  var valueP = 0
  var valueD = 0
  var i = 2
  var j = 2
  var playerHand = new ListBuffer[Card]()
  var dealerHand = new ListBuffer[Card]()
  def processInput(input: String): Unit = {
    input match {
      case "n" =>
        println("Started a new game!")
        dealer.generateDealerCards
        //println("Generated " + dealer.generateDealerCards.size + " Cards")
        println("Cards Player: ")
        playerHand += dealer.drawCard()
        playerHand += dealer.drawCard()
        valueP += playerHand.head.value
        valueP += playerHand(1).value
        println(playerHand.head.toString +" and "+ playerHand(1).toString)
        println("Value: " + valueP)
        println("Cards Dealer: ")
        dealerHand += dealer.drawCard()
        dealerHand += dealer.drawCard()
        valueD += playerHand.head.value
        //valueP += playerHand(1).value
        println(dealerHand.head)
        println("Value: " + valueD)
      case "exit" =>
        println("Exiting Blackjack...")
        System.exit(0)
      case "Scala ist toll!" =>
        println("Sag mir was neues\n (☞ﾟヮﾟ)☞ ")
      case "t" =>
        for {
          card <- CardDeck().cardDeck
        } yield println(card)
      case "p" =>
        valueD += dealerHand(1).value
        println("No new Card for the Player, Dealers turn:")
        println(dealerHand(1).toString)
        println("Dealer Value: " + valueD)
        while(valueD < 17) {
          dealerHand += dealer.drawCard()
          valueD += dealerHand(j).value
          println(dealerHand(j))
          j += 1
          println("New Value Dealer: " + valueD)
        }
        if(valueD == valueP){
          println("No one wins.\nDraw!")
          System.exit(0)
        }
        if(valueD > 21 || valueD < valueP) {
          println("You WIN!")
          System.exit(0)
        } else {
          println("You LOSE!")
          System.exit(0)
        }
      case "td" =>
        println("Draw Card:")
        if(valueP <= 21) {
          playerHand += dealer.drawCard()
          valueP += playerHand(i).value
          println(playerHand(i))
          println("New Value: " + valueP)
          i += 1
          //println(i)
        }
        if(valueP == 21) {
          println("Blackjack!\nYou WIN!")
          System.exit(0)
        }
        if(valueP > 21) {
          println("You loose!")
          System.exit(0)
        }
      case _ =>
        println("Input not recognized!")
    }
  }
}
