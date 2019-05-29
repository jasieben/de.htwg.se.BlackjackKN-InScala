package de.htwg.se.blackjackKN.model

case class Bet(value: Double = 100) {
  def getValue : Double = {
    value
  }
}
