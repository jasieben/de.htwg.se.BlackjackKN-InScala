package de.htwg.se.blackjackKN.playerManagement.model.persistenceManagerComponent.mongodbImplementation

import de.htwg.se.blackjackKN.playerManagement.model.{Bet, Player}
import de.htwg.se.blackjackKN.playerManagement.model.persistenceManagerComponent.PlayerPersistenceInterface
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.types.ObjectId
import org.mongodb.scala.bson.codecs.Macros
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.Filters._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class PlayerPersistence extends PlayerPersistenceInterface {

  val uri: String = "mongodb://root:123@mongodb/playermanagement?retryWrites=true&w=majority"

  val client: MongoClient = MongoClient(uri)
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(Macros.createCodecProviderIgnoreNone[Player],
    Macros.createCodecProviderIgnoreNone[Bet](), EnumerationCodecProvider), MongoClient.DEFAULT_CODEC_REGISTRY)
  val db: MongoDatabase = client.getDatabase("playermanagement").withCodecRegistry(codecRegistry)
  val players: MongoCollection[Player] = db.getCollection("players")

  override def create(player: Player): Player = {
    val playerWithId = player.copy(id = Some(ObjectId.get().toString))
    players.insertOne(playerWithId).toFuture()
    playerWithId
  }

  override def update(player: Player): Unit = {
    var seq = Seq(
      set("name", player.name),
      set("balance", player.balance))

    if (player.bet.nonEmpty) {
      seq = seq :+ set("bet", player.bet.get)
    }

    players.updateOne(equal("_id", player.id.getOrElse(throw new RuntimeException("Can't update element without Id"))), seq)
      .toFuture()
  }

  override def load(playerId: String): Option[Player] = {
    Await.result(players.find(equal("_id", playerId)).first().toFutureOption(), Duration("10s"))
  }

  override def delete(player: Player): Unit = {
    players.deleteOne(equal("_id", player.id.get)).toFuture()
  }
}
