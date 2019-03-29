package de.htwg.se.blackjackKN

import de.htwg.se.blackjackKN.model.Player

object BlackjackKN {
  def main(args: Array[String]): Unit = {
    val student = Player("Benni und Jana")
    println("Hello, " + student.name + "!")
    println("Ich bin das Spiel Blackjack!")
  }
}