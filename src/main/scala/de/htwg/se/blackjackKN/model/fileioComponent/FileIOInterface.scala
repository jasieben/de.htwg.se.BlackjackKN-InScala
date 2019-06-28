package de.htwg.se.blackjackKN.model.fileioComponent

import de.htwg.se.blackjackKN.model.personsComponent.PlayerInterface

trait FileIOInterface {
  def load(playerName : String) : PlayerInterface
  def store(player: PlayerInterface) : Boolean
}
