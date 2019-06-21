package de.htwg.se.blackjackKN.controller.ControllerBaseImpl

import de.htwg.se.blackjackKN.controller.GameState.GameState
import de.htwg.se.blackjackKN.model.personsComponent.PlayerInterface

trait BetHandler {
  def handleRequest(state : GameState, player: PlayerInterface): Unit = {}
  val successor: BetHandler
}
