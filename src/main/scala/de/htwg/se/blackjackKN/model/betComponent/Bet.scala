package de.htwg.se.blackjackKN.model.betComponent

case class Bet(value: Double = 100) {
  def getValue : Double = {
    value
  }
}