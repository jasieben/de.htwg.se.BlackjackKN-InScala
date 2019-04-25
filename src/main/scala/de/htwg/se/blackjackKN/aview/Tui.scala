package de.htwg.se.blackjackKN.aview

import de.htwg.se.blackjackKN.model.{CardDeck, Dealer}

class Tui {
  val dealer = Dealer()
  def processInput(input: String): Unit = {
    input match {
      case "n" =>
        println("Started a new game!")
        println("Generated " + dealer.generateDealerCards.size + " Cards")
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
        println(dealer.drawCard())

      case _ =>
        println("Input not recognized!")
    }
  }
}
