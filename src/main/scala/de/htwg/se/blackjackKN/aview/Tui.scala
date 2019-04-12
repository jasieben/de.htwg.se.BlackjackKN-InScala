package de.htwg.se.blackjackKN.aview

import de.htwg.se.blackjackKN.model.{CardDeck, FaceCard}

class Tui {
  def processInput(input: String): Unit = {
    input match {
      case "n" =>
        println("Started a new game!")
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
