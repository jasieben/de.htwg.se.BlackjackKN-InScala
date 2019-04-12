package de.htwg.se.blackjackKN

import de.htwg.se.blackjackKN.aview.Tui
import de.htwg.se.blackjackKN.model.Player
import scala.io.StdIn.readLine

object BlackjackKN {
  def main(args: Array[String]): Unit = {
    val student = Player("Benni und Jana")
    println("Hello, " + student.name + "!")
    println("Ich bin das Spiel Blackjack!")

    val tui = new Tui
    var input: String = ""
    do {
      input = readLine()
      tui.processInput(input)
    } while (true)
  }
}