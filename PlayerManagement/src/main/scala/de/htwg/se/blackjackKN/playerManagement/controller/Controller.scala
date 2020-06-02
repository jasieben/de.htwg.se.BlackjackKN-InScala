package de.htwg.se.blackjackKN.playerManagement.controller

import de.htwg.se.blackjackKN.playerManagement.model.Player
import play.api.libs.json.Json
import spray.json.{JsNumber, JsValue}

class Controller {
  def createNewPlayer(data: JsValue): String = {
    val jsonData = data.asJsObject
    val name: String = jsonData.fields("name").toString()
    val player = Player(123, name)
    Json.obj(
      "success" -> true,
      "id" -> player.id,
      "name" -> player.name,
      "balance" -> player.balance
    ).toString()
  }

  def getPlayer(id: Int): String = {
    val player = Player(123, "Test")
    Json.obj(
      "id" -> player.id,
      "name" -> player.name,
      "balance" -> player.balance
    ).toString()
  }

  def newBet(playerId: Int, data: JsValue): String = {
    val betValue: Int = data.asJsObject.fields("betValue")[JsNumber]

    val player = Player(playerId)
    player.newBet(betValue)
    Json.obj(
      "success" -> true,
      "balance" -> player.balance
    ).toString()
  }
}
