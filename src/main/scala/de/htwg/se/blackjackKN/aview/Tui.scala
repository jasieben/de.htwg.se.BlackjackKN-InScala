package de.htwg.se.blackjackKN.aview

import de.htwg.se.blackjackKN.model.{CardDeck, Dealer}

class Tui {
  def processInput(input: String): Unit = {
    val dealer = Dealer()
    input match {
      case "n" =>
        println("Started a new game!")
        println(dealer.generateDealerCards.size)
      case "q" =>
        println("Exiting Blackjack...")
        System.exit(0)
      case "Scala ist toll!" =>
        println("Sag mir was neues\n (☞ﾟヮﾟ)☞ ")
      case "t" =>
        for {
          card <- CardDeck().cardDeck
        } yield println(card)
      case _ =>
        println("Input not recognized!")
    }
  }
}
