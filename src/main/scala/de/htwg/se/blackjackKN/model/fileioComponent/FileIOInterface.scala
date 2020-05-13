package de.htwg.se.blackjackKN.model.fileioComponent

import de.htwg.se.blackjackKN.model.personsComponent.Player

trait FileIOInterface {
  def load(playerName : String) : Option[Player]
  def store(player: Player) : Boolean
}
