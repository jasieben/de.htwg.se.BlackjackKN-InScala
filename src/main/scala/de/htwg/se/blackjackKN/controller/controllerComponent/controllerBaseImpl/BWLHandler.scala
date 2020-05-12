package de.htwg.se.blackjackKN.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.blackjackKN.controller.controllerComponent.GameState
import de.htwg.se.blackjackKN.controller.controllerComponent.GameState.GameState
import de.htwg.se.blackjackKN.model.personsComponent.PlayerInterface

class WinningHandler(val successor: Option[BetHandler]) extends BetHandler {
  override def handleRequest(gameState: GameState, player: PlayerInterface): Unit = {
    if (gameState == GameState.PLAYER_WINS)
      player.balance += player.bet.value * 2
  }
}

class LoosingHandler(val successor: Option[BetHandler]) extends BetHandler {
  override def handleRequest(gameState: GameState, player: PlayerInterface): Unit = {
    if (!(gameState == GameState.PLAYER_LOOSE))
      successor.get.handleRequest(gameState, player)
  }
}

class BlackjackHandler(val successor: Option[BetHandler]) extends BetHandler {
  override def handleRequest(gameState: GameState, player: PlayerInterface): Unit = {
    if (gameState == GameState.PLAYER_BLACKJACK)
      player.balance += player.bet.value * 2.5
    else
      successor.get.handleRequest(gameState, player)
  }
}

class PushHandler(val successor: Option[BetHandler]) extends BetHandler {
  override def handleRequest(gameState: GameState, player: PlayerInterface): Unit = {
    if (gameState == GameState.PUSH)
      player.balance += player.bet.value
    else
      successor.get.handleRequest(gameState, player)
  }
}
