package de.htwg.se.blackjackKN.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.blackjackKN.controller.controllerComponent.GameState
import de.htwg.se.blackjackKN.controller.controllerComponent.GameState.GameState
import de.htwg.se.blackjackKN.model.personsComponent.Player

class WinningHandler(val successor: Option[BetHandler]) extends BetHandler {
  override def handleRequest(gameState: GameState, player: Player): Player = {
    if (gameState == GameState.PLAYER_WINS)
      player.changeBalance(player.balance + player.bet.get.value * 2)
    else
      player
  }
}

class LoosingHandler(val successor: Option[BetHandler]) extends BetHandler {
  override def handleRequest(gameState: GameState, player: Player): Player = {
    if (!(gameState == GameState.PLAYER_LOOSE))
      successor.get.handleRequest(gameState, player)
    else
      player
  }
}

class BlackjackHandler(val successor: Option[BetHandler]) extends BetHandler {
  override def handleRequest(gameState: GameState, player: Player): Player = {
    if (gameState == GameState.PLAYER_BLACKJACK)
      player.changeBalance((player.balance + player.bet.get.value * 2.5).toInt)
    else
      successor.get.handleRequest(gameState, player)
  }
}

class PushHandler(val successor: Option[BetHandler]) extends BetHandler {
  override def handleRequest(gameState: GameState, player: Player): Player = {
    if (gameState == GameState.PUSH)
      player.changeBalance(player.balance + player.bet.get.value)
    else
      successor.get.handleRequest(gameState, player)
  }
}
