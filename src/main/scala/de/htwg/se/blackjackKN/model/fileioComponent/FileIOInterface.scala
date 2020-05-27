package de.htwg.se.blackjackKN.model.fileioComponent

trait FileIOInterface {
  def load(playerName : String) : Option[Player]
  def store(player: Player) : Boolean
}
