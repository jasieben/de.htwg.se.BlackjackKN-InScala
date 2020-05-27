package de.htwg.se.blackjackKN.playerManagement.model.fileioComponent

import de.htwg.se.blackjackKN.playerManagement.model.Player

trait FileIOInterface {
  def load(playerName : String) : Option[Player]
  def store(player: Player) : Boolean
}
