package de.htwg.se.blackjackKN.playerManagement.model.persistenceManagerComponent

import de.htwg.se.blackjackKN.playerManagement.model.Player

trait PlayerPersistenceInterface {
  def create(player: Player): Player

  def update(player: Player): Unit

  def load(playerId: Int): Option[Player]

  def delete(player: Player)
}
