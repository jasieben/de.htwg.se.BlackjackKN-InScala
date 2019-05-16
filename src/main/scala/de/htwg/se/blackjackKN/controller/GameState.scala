package de.htwg.se.blackjackKN.controller

object GameState extends Enumeration {
  type GameState = Value
  val WAITING_FOR_INPUT, DEALER_BUST, PLAYER_BUST, DEALER_WINS, PLAYER_WINS = Value
}
