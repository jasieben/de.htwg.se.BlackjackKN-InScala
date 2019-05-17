package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.controller.GameState.GameState
import de.htwg.se.blackjackKN.model.Player

trait BetHandler {
  def handleRequest(state : GameState, player: Player): Unit = {}
  val successor: BetHandler
}
