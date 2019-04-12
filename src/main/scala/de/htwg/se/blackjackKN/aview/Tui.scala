package de.htwg.se.blackjackKN.aview

class Tui {
  def processInput(input: String): Unit = {
    input match {
      case "n" =>
        println("Started a new game!")
      case "q" =>
        println("Exiting Blackjack...")
        System.exit(0)
      case _ =>
        println("Input not recognized!")
    }
  }

}
