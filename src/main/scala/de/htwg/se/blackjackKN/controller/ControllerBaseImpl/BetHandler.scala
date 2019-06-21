package de.htwg.se.blackjackKN.controller.ControllerBaseImpl

import de.htwg.se.blackjackKN.controller.GameState.GameState
import de.htwg.se.blackjackKN.model.personsComponent.personsInterface.Player

trait BetHandler {
  def handleRequest(state : GameState, player: Player): Unit = {}
  val successor: BetHandler
}
