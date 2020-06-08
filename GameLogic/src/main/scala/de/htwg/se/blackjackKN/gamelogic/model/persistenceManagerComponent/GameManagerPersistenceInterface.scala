package de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent

import de.htwg.se.blackjackKN.gamelogic.model.GameManager

trait GameManagerPersistenceInterface {
  def create(gameManager: GameManager): Int

  def update(gameManager: GameManager): Unit

  def load(gameManagerId: Int)

  def deleteGameManager(gameManager: GameManager)
}
