package de.htwg.se.blackjackKN.aview

import de.htwg.se.blackjackKN.model.{Card, CardDeck, Dealer}

import scala.collection.mutable.ListBuffer

class Tui {
  val dealer = Dealer()
  var valueP = 0
  var valueD = 0
  var i = 2
  var playerHand = new ListBuffer[Card]()
  def processInput(input: String): Unit = {
    input match {
      case "n" =>
        println("Started a new game!")
        println("Generated " + dealer.generateDealerCards.size + " Cards")
        println("Cards Player: ")
        playerHand += dealer.drawCard()
        playerHand += dealer.drawCard()
        valueP += playerHand.head.value
        valueP += playerHand(1).value
        println(playerHand)
        println(valueP)
        println("Cards Dealer: ")
        val dealerHand : List[Card] = List(dealer.drawCard(), dealer.drawCard())
        println(dealerHand.head)
        println(dealerHand.head.value)
      case "q" =>
        println("Exiting Blackjack...")
        System.exit(0)
      case "Scala ist toll!" =>
        println("Sag mir was neues\n (☞ﾟヮﾟ)☞ ")
      case "t" =>
        for {
          card <- CardDeck().cardDeck
        } yield println(card)
      case "td" =>
        println("Draw Card:")
        if(valueP <= 21) {
          playerHand += dealer.drawCard()
          valueP += playerHand(i).value
          println(playerHand(i))
          println("Value: " + valueP)
          i += 1
          //println(i)
        }
        if(valueP > 21) {
          println("You loose!")
        }
      case _ =>
        println("Input not recognized!")
    }
  }
}
