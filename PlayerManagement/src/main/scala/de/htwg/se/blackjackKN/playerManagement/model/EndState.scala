package de.htwg.se.blackjackKN.playerManagement.model

object EndState extends Enumeration {
  type EndState = Value
  val DEALER_WINS, PLAYER_WINS, PLAYER_LOOSE, PLAYER_BLACKJACK, DEALER_BLACKJACK, PUSH, ONGOING, START = Value
}
