package de.htwg.se.blackjackKN.playerManagement.model.persistenceManagerComponent.slickImplementation

import de.htwg.se.blackjackKN.playerManagement.model.{Bet, Player}
import de.htwg.se.blackjackKN.playerManagement.model.persistenceManagerComponent.PlayerPersistenceInterface
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class PlayerPersistence extends PlayerPersistenceInterface {

  val db = Database.forURL(
    "jdbc:postgresql://database:5432/playermanagement",
    "root", "123",
    null,
    "org.postgresql.Driver")

  val players = TableQuery[Players]
  val bets = TableQuery[Bets]

  val setup = DBIO.seq(
    players.schema.createIfNotExists,
    bets.schema.createIfNotExists
  )
  db.run(setup)

  override def create(player: Player): Player = {
    val gameIdQuery = (players returning players.map(_.id)) += (None, player.name, player.balance, Some(0))
    val gameId = Await.result(db.run(gameIdQuery), Duration("10s"))
    player.copy(id = Some(gameId))
  }

  override def update(player: Player): Unit = {
    var betId = -1
    if (player.bet.nonEmpty) {
      val q = bets.filter(_.id === player.bet.get.id).distinct.result.headOption
      val betOption = Await.result(db.run(q), Duration("10s"))
      if (betOption.isEmpty) {
        val newBetIdQuery = (bets returning bets.map(_.id)) += player.bet.get
        val newBetId = Await.result(db.run(newBetIdQuery), Duration("10s"))
        betId = newBetId
      } else {
        betId = player.bet.get.id.get
        val updateBetQ = bets.filter(_.id === betId).update(player.bet.get)
        db.run(updateBetQ)
      }
    }
    val q = players.filter(_.id === player.id)
    val updateAction = q.update((Some(player.id.get), player.name, player.balance, if (betId == -1) None else Some(betId)))
    db.run(updateAction)
  }

  override def load(playerId: Int): Option[Player] = {
    var bet: Option[Bet] = None
    val query = players.filter(_.id === playerId)
    val playerTuple = Await.result(db.run(query.result.headOption), Duration("10s"))
    if (playerTuple.isEmpty) {
      return None
    }
    if (playerTuple.get._4.nonEmpty && playerTuple.get._4.get != 0) {
      val betOption = Await.result(db.run(bets.filter(_.id === playerTuple.get._4.get).result.headOption), Duration("10s"))
      if (betOption.isEmpty) {
        throw new RuntimeException("Could not find bet with id " + playerTuple.get._4.get)
      } else {
        bet = Some(betOption.get)
      }
    }
    Some(Player(playerTuple.get._1, playerTuple.get._2, playerTuple.get._3, bet))
  }

  override def delete(player: Player): Unit = {
    val q = players.filter(_.id === player.id).delete
    db.run(q)
  }
}
