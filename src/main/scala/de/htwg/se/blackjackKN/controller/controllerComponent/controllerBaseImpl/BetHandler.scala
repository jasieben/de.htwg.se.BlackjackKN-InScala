package de.htwg.se.blackjackKN.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.blackjackKN.controller.controllerComponent.GameState.GameState
import de.htwg.se.blackjackKN.model.personsComponent.Player

trait BetHandler {
  def handleRequest(state : GameState, player: Player): Player
  val successor: Option[BetHandler]
}
