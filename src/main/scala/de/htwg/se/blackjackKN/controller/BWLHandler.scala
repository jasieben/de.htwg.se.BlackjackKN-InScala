package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.model.Player

class WinningHandler {
  def handleRequest(player: Player): Unit = {
    player.balance
  }
}

class LoosingHandler {
  def handleRequest(player: Player): Unit = {

  }
}

class BlackjackHandler {
  def handleRequest(player: Player): Unit = {

  }
}
