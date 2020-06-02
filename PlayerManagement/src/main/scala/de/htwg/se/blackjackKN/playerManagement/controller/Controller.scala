package de.htwg.se.blackjackKN.playerManagement.controller

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.complete
import de.htwg.se.blackjackKN.playerManagement.model.{Bet, EndState, Player}
import play.api.libs.json.{JsValue, Json}

class Controller {

  val win = new WinningHandler(None)
  val loose = new LoosingHandler(Option(win))
  val blackjack = new BlackjackHandler(Option(loose))
  val push = new PushHandler(Option(blackjack))

  var player: Player = Player()

  def createNewPlayer(data: JsValue): String = {
    val name: String = (data \ "name").as[String]
    player = Player(123, name)
    Json.obj(
      "success" -> true,
      "id" -> player.id,
      "name" -> player.name,
      "balance" -> player.balance
    ).toString()
  }

  def getPlayer(id: Int): String = {
    Json.obj(
      "id" -> player.id,
      "name" -> player.name,
      "balance" -> player.balance
    ).toString()
  }

  def newBet(playerId: Int, data: JsValue): String = {
    val betValue: Int = (data \ "betValue").as[Int]

    player = player.newBet(betValue)
    player.bet match  {
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

  def resolveBet(playerId: Int, data: JsValue): String = {
    val gameState: String = (data \ "gameState").as[String]
    val endState = EndState.withName(gameState)
    this.push.handleRequest(endState, player)
    Json.obj(
      "success" -> true,
      "balance" -> player.balance,
      "betValue" -> player.bet.get.value
    ).toString()
  }
}
