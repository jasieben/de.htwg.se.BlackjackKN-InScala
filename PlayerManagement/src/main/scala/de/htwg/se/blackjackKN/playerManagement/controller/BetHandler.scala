package de.htwg.se.blackjackKN.playerManagement.controller

import de.htwg.se.blackjackKN.playerManagement.model.Player
import de.htwg.se.blackjackKN.playerManagement.model.EndState.EndState

trait BetHandler {
  def handleRequest(state : EndState, player: Player): Player
  val successor: Option[BetHandler]
}
