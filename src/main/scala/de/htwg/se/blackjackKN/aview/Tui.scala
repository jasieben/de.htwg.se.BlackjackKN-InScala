package de.htwg.se.blackjackKN.aview

class Tui {
  def processInput(input: String): Unit = {
    input match {
      case "n" =>
        println("Neues Spiel gestartet!")
      case "q" =>
        println("BlackjackKN wird beendet...")
        System.exit(0)
    }
  }

}
