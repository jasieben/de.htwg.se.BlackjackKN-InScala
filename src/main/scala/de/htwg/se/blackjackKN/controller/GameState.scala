package de.htwg.se.blackjackKN.controller

object GameState extends Enumeration {
  type GameState = Value
  val WAITING_FOR_INPUT, DEALER_BUST, PLAYER_BUST, DEALER_WINS, PLAYER_WINS, PLAYER_LOOSE, PLAYER_BLACKJACK, IDLE
      , HIT, STAND, SHUFFLING, FIRST_ROUND, REVEAL, DEALER_DRAWS, ACE, PUSH, BET_SET, BET_FAILED = Value
}
