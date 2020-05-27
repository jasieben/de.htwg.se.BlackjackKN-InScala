package de.htwg.se.blackjackKN.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.blackjackKN.controller.controllerComponent.GameState.GameState

trait BetHandler {
  def handleRequest(state : GameState, player: Player): Player
  val successor: Option[BetHandler]
}
