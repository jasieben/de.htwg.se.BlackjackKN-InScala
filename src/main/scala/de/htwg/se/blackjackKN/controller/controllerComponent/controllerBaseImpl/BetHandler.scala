package de.htwg.se.blackjackKN.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.blackjackKN.controller.controllerComponent.GameState.GameState
import de.htwg.se.blackjackKN.model.personsComponent.PlayerInterface

trait BetHandler {
  def handleRequest(state : GameState, player: PlayerInterface): Unit = {}
  val successor: BetHandler
}
