package de.htwg.se.BlackjackKN

import de.htwg.se.BlackjackKN.model.Player

object BlackjackKN {
  def main(args: Array[String]): Unit = {
    val student = Player("Benni und Jana")
    println("Hello, " + student.name + "!")
    println("Ich bin das Spiel Blackjack!")
  }
}