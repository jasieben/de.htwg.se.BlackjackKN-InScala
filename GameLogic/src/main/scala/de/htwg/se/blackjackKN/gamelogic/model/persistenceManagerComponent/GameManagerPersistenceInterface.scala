package de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent

import de.htwg.se.blackjackKN.gamelogic.model.GameManager

trait GameManagerPersistenceInterface {
  def create(gameManager: GameManager): GameManager

  def update(gameManager: GameManager): Unit

  def load(playerId: String): Option[GameManager]

  def loadEmptySession(): Option[GameManager]

  def deleteGameManager(gameManager: GameManager)
}
