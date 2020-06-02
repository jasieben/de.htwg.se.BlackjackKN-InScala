package de.htwg.se.blackjackKN.playerManagement.controller

import de.htwg.se.blackjackKN.playerManagement.model.{EndState, Player}
import de.htwg.se.blackjackKN.playerManagement.model.EndState.EndState

class WinningHandler(val successor: Option[BetHandler]) extends BetHandler {
  override def handleRequest(gameState: EndState, player: Player): Player = {
    if (gameState == EndState.PLAYER_WINS)
      player.changeBalance(player.balance + player.bet.get.value * 2)
    else
      player
  }
}

class LoosingHandler(val successor: Option[BetHandler]) extends BetHandler {
  override def handleRequest(gameState: EndState, player: Player): Player = {
    if (!(gameState == EndState.PLAYER_LOOSE))
      successor.get.handleRequest(gameState, player)
    else
      player
  }
}

class BlackjackHandler(val successor: Option[BetHandler]) extends BetHandler {
  override def handleRequest(gameState: EndState, player: Player): Player = {
    if (gameState == EndState.PLAYER_BLACKJACK)
      player.changeBalance((player.balance + player.bet.get.value * 2.5).toInt)
    else
      successor.get.handleRequest(gameState, player)
  }
}

class PushHandler(val successor: Option[BetHandler]) extends BetHandler {
  override def handleRequest(gameState: EndState, player: Player): Player = {
    if (gameState == EndState.PUSH)
      player.changeBalance(player.balance + player.bet.get.value)
    else
      successor.get.handleRequest(gameState, player)
  }
}
