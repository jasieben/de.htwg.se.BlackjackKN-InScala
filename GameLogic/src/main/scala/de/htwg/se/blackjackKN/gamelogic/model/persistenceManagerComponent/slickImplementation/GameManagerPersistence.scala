package de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent.slickImplementation

import de.htwg.se.blackjackKN.gamelogic.model.GameManager
import de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent.GameManagerPersistenceInterface
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class GameManagerPersistence extends GameManagerPersistenceInterface {

  val db = Database.forURL(
    "jdbc:postgresql://database:5432/gamelogic",
    "root", "123",
    null,
    "org.postgresql.Driver")

  val sessions = TableQuery[Sessions]

  val setup = sessions.schema.createIfNotExists
  db.run(setup)

  override def create(gameManager: GameManager): GameManager = {
    val gameIdQuery = (sessions returning sessions.map(_.id)) += gameManager
    val gameId = Await.result(db.run(gameIdQuery), Duration("10s"))
    gameManager.copy(id = Some(gameId))
  }

  override def update(gameManager: GameManager): Unit = {
    val q = sessions.filter(_.id === gameManager.id)
    val updateAction = q.update(gameManager)
    db.run(updateAction)
  }

  override def load(playerId: String): Option[GameManager] = {
    val query = sessions.filter(_.currentPlayerInRound === playerId)
    Await.result(db.run(query.result.headOption), Duration("10s"))
  }

  override def loadEmptySession(): Option[GameManager] = {
    val query = sessions.filter(_.currentPlayerInRound === "")
    Await.result(db.run(query.result.headOption), Duration("10s"))
  }

  override def deleteGameManager(gameManager: GameManager): Unit = {
    val q = sessions.filter(_.id === gameManager.id).delete
    db.run(q)
  }
}
