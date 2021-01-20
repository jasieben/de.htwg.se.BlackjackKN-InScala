package de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent.mongodbImplementation

import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.GameState
import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.GameState.GameState
import de.htwg.se.blackjackKN.gamelogic.model.GameManager
import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.CardInterface
import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.cardsBaseImpl.{FaceCard, NumberCard}
import de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent.GameManagerPersistenceInterface
import org.bson.{BsonReader, BsonWriter}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.Filters._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.{CodecProvider, CodecRegistry}
import org.bson.types.ObjectId
import org.mongodb.scala.bson.codecs.Macros
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GameManagerPersistence extends GameManagerPersistenceInterface {
  private val databaseString = sys.env.getOrElse("DATABASE", "mongodb://root:123@localhost/gamelogic?retryWrites=true&w=majority")
  val uri: String = databaseString

  val client: MongoClient = MongoClient(uri)
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[GameManager], EnumerationCodecProvider, classOf[FaceCard], classOf[NumberCard], CardInterfaceCodecProvider) , MongoClient.DEFAULT_CODEC_REGISTRY)
  val db: MongoDatabase = client.getDatabase("gamelogic").withCodecRegistry(codecRegistry)
  val sessions: MongoCollection[GameManager] = db.getCollection("sessions")

  override def create(gameManager: GameManager): GameManager = {

    val newGameManager = gameManager.copy(id = Some(ObjectId.get().toString))
    sessions.insertOne(newGameManager).toFuture()
    newGameManager
  }

  override def update(gameManager: GameManager): Unit = {
    sessions.updateOne(equal("_id", gameManager.id.getOrElse(throw new RuntimeException("Can't update element without Id"))), Seq(
      set("dealerHand", gameManager.dealerHand),
      set("playerHands", gameManager.playerHands),
      set("cardDeck", gameManager.cardDeck),
      set("gameStates", gameManager.gameStates),
      set("revealed", gameManager.revealed),
      set("currentPlayerInRound", gameManager.currentPlayerInRound))).toFuture()
  }

  override def load(playerId: String): Option[GameManager] = {
    Await.result(sessions.find(equal("currentPlayerInRound", playerId)).first().toFutureOption(), Duration("10s"))
  }

  override def loadEmptySession(): Option[GameManager] = {
    Await.result(sessions.find(where("this.currentPlayerInRound.length < 3")).first().toFutureOption(), Duration("10s"))
  }

  override def deleteGameManager(gameManager: GameManager): Unit = {
    Await.result(sessions.deleteOne(equal("_id", gameManager.id.get)).toFuture(), Duration("10s"))
  }
}



