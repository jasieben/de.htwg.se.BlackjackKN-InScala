package de.htwg.se.blackjackKN.playerManagement.controller

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.complete
import com.google.inject.Guice
import de.htwg.se.blackjackKN.playerManagement.PlayerManagmentModule
import de.htwg.se.blackjackKN.playerManagement.model.persistenceManagerComponent.PlayerPersistenceInterface
import de.htwg.se.blackjackKN.playerManagement.model.{Bet, EndState, Player}
import play.api.libs.json.{JsValue, Json}

class Controller {

  val injector = Guice.createInjector(new PlayerManagmentModule)
  val playerPersistence = injector.getInstance(classOf[PlayerPersistenceInterface])
  val win = new WinningHandler(None)
  val loose = new LoosingHandler(Option(win))
  val blackjack = new BlackjackHandler(Option(loose))
  val push = new PushHandler(Option(blackjack))

  var player: Player = Player()

  def loadPlayer(playerId: String): Option[Player] = {
    playerPersistence.load(playerId)
  }

  def createNewPlayer(data: JsValue): String = {
    val name: String = (data \ "name").as[String]
    player = Player(None, name)

    player = playerPersistence.findByName(name).getOrElse(
      playerPersistence.create(player)
    )

    Json.obj(
      "success" -> true,
      "id" -> player.id,
      "name" -> player.name,
      "balance" -> player.balance
    ).toString()
  }

  def getPlayer(id: String): String = {
    val playerOption = loadPlayer(id)
    if (playerOption.isEmpty) {
      return Json.obj(
        "success" -> false,
        "msg" -> s"Player with $id could not be found"
      ).toString()
    }
    player = playerOption.get
    Json.obj(
      "success" -> true,
      "id" -> player.id,
      "name" -> player.name,
      "balance" -> player.balance
    ).toString()
  }

  def newBet(playerId: String, data: JsValue): String = {
    val betValue: Int = (data \ "betValue").as[Int]
    val playerOption = loadPlayer(playerId)
    if (playerOption.isEmpty) {
      return Json.obj(
        "success" -> false,
        "msg" -> s"Player with $playerId could not be found"
      ).toString()
    }
    player = playerOption.get
    player = player.newBet(betValue)
    playerPersistence.update(player)
    player.bet match {
      case Some(bet) =>
        Json.obj(
          "success" -> true,
          "balance" -> player.balance,
          "betValue" -> bet.value
        ).toString()
      case None =>
        Json.obj(
          "success" -> false,
          "balance" -> player.balance,
          "msg" -> "BET VALUE TOO HIGH"
        ).toString()
    }
  }

  def resolveBet(playerId: String, data: JsValue): String = {
    val gameState: String = (data \ "gameState").as[String]
    val playerOption = loadPlayer(playerId)
    if (playerOption.isEmpty) {
      return Json.obj(
        "success" -> false,
        "msg" -> s"Player with $playerId could not be found"
      ).toString()
    }
    player = playerOption.get
    val endState = EndState.withName(gameState)
    player = this.push.handleRequest(endState, player)
    playerPersistence.update(player)
    Json.obj(
      "success" -> true,
      "balance" -> player.balance,
      "betValue" -> player.bet.get.value
    ).toString()
  }
}
