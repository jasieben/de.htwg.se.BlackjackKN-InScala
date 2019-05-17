package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.controller.GameState.GameState

trait BetHandler {
  def handleRequest(state : GameState): Unit = {
    state match {
      case GameState.PLAYER_WINS =>

    }
  }
}
