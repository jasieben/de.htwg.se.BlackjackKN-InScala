package de.htwg.se.blackjackKN.controller.ControllerBaseImpl

import de.htwg.se.blackjackKN.controller.GameState
import de.htwg.se.blackjackKN.controller.GameState.GameState
import de.htwg.se.blackjackKN.model.Player

class WinningHandler(val successor: BetHandler) extends BetHandler {
  override def handleRequest(gameState: GameState, player: Player): Unit = {
    if (gameState == GameState.PLAYER_WINS)
      player.balance += player.bet.value * 2
  }
}

class LoosingHandler(val successor: BetHandler) extends BetHandler {
  override def handleRequest(gameState: GameState, player: Player): Unit = {
    if (!(gameState == GameState.PLAYER_LOOSE))
      successor.handleRequest(gameState, player)
  }
}

class BlackjackHandler(val successor: BetHandler) extends BetHandler {
  override def handleRequest(gameState: GameState, player: Player): Unit = {
    if (gameState == GameState.PLAYER_BLACKJACK)
      player.balance += player.bet.value * 2.5
    else
      successor.handleRequest(gameState, player)
  }
}

class PushHandler(val successor: BetHandler) extends BetHandler {
  override def handleRequest(gameState: GameState, player: Player): Unit = {
    if (gameState == GameState.PUSH)
      player.balance += player.bet.value
    else
      successor.handleRequest(gameState, player)
  }
}
