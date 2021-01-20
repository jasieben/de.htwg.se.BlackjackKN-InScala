package de.htwg.se.blackjackKN.gamelogic.aview

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{as, complete, concat, entity, get, path, pathPrefix, post, put}
import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.{ControllerInterface, GameState}
import play.api.libs.json.{JsArray, JsValue, Json}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

class RestApi(controller: ControllerInterface) {
  val connectionInterface = "0.0.0.0"
  val connectionPort = 9001

  def run() {
    implicit val actorSystem: ActorSystem = ActorSystem("actorSystem")
    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

    val route = concat(
      pathPrefix("game") {
        concat(
          path("matchmaking") {
            concat(
              put(
                entity(as[String]) { jsonString => {
                  val json = Json.parse(jsonString)
                  complete(HttpEntity(ContentTypes.`application/json`, matchmake(json)))
                }
                }
              )
            )
          },
          path("start") {
            concat(
              put(
                entity(as[String]) { jsonString => {
                  val json = Json.parse(jsonString)
                  complete(HttpEntity(ContentTypes.`application/json`, startGameRoute(json)))
                }
                }
              )
            )
          },
          path("hit") {
            concat(
              put(
                entity(as[String]) { jsonString => {
                  val json = Json.parse(jsonString)
                  complete(HttpEntity(ContentTypes.`application/json`, hitRoute(json)))
                }
                }
              )
            )
          },
          path("stand") {
            concat(
              put(
                entity(as[String]) { jsonString => {
                  val json = Json.parse(jsonString)
                  complete(HttpEntity(ContentTypes.`application/json`, standRoute(json)))
                }
                }
              )
            )
          }
        )
      }
    )

    val bindingFuture = Http().bindAndHandle(route, connectionInterface, connectionPort)

    println(s"Server online at http://$connectionInterface:$connectionPort/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => actorSystem.terminate()) // and shutdown when done
  }

  private def matchmake(json: JsValue): String = {
    val playerId = (json \ "playerId").as[String]
    val betValue = (json \ "betValue").as[Int]
    if (controller.loadGameManager(playerId)) {
      return Json.obj(
        "success" -> false,
        "msg" -> "Player already has a game in progress.",
        "sessionId" -> controller.gameManager.id
      ).toString()
    }
    controller.loadNewGameManager(playerId)
    controller.setBet(playerId, betValue)
    if (controller.gameManager.gameStates(controller.currentPlayerIndex).isEmpty || controller.gameManager.gameStates(controller.currentPlayerIndex).last == GameState.BET_FAILED) {
      return Json.obj(
        "success" -> false,
        "msg" -> "Failed to set bet.",
        "gameStates" -> gameStatesToJsonArray(0)
      ).toString()
    }
    Json.obj(
      "success" -> true,
      "gameStates" -> gameStatesToJsonArray(0),
      "players" -> controller.gameManager.currentPlayerInRound,
      "playerIndex" -> controller.currentPlayerIndex
    ).toString()
  }

  def startGameRoute(json: JsValue): String = {
    val playerId = (json \ "playerId").as[String]
    if (!controller.loadGameManager(playerId)) {
      return Json.obj(
        "success" -> false,
        "msg" -> "Player doesn't have a game in progress."
      ).toString()
    }
    var revealed = false

    val currentGamestate = controller.gameManager.gameStates(controller.currentPlayerIndex).size

    controller.startNewRound(playerId, controller.gameManager.id)

    if (controller.gameManager.gameStates(controller.currentPlayerIndex).exists(p => p.equals(GameState.REVEAL) || p.equals(GameState.PLAYER_BUST) || p.equals(GameState.DEALER_BUST))) {
      revealed = true
    }

    Json.obj(
      "playerCards" -> getAllPlayerCards(),
      "dealerCards" -> (if (revealed) getAllDealerCards() else Json.arr()),
      "playerCardsValue" -> controller.gameManager.getPlayerHandValue(controller.currentPlayerIndex),
      "dealerCardsValue" -> (if (revealed) controller.gameManager.getDealerHandValue else "-"),
      "gameStates" -> gameStatesToJsonArray(currentGamestate)
    ).toString()
  }

  def hitRoute(json: JsValue): String = {
    val playerId = (json \ "playerId").as[String]

    if (!controller.loadGameManager(playerId)) {
      return Json.obj(
        "success" -> false,
        "msg" -> "No game available"
      ).toString()
    }

    val currentGamestate = controller.gameManager.gameStates(controller.currentPlayerIndex).size
    controller.hitCommand(playerId)

    var revealed = false

    if (controller.gameManager.gameStates(controller.currentPlayerIndex).exists(p => p.equals(GameState.REVEAL) || p.equals(GameState.PLAYER_BUST) || p.equals(GameState.DEALER_BUST))) {
      revealed = true
    }

    Json.obj(
      "success" -> true,
      "hitCard" -> controller.gameManager.getPlayerCard(controller.currentPlayerIndex, controller.gameManager.getPlayerHandSize(controller.currentPlayerIndex) - 1).toString,
      "dealerCards" -> (if (revealed) getAllDealerCards() else Json.arr()),
      "playerCardsValue" -> controller.gameManager.getPlayerHandValue(controller.currentPlayerIndex),
      "dealerCardsValue" -> (if (revealed) controller.gameManager.getDealerHandValue else ""),
      "gameStates" -> gameStatesToJsonArray(currentGamestate)
    ).toString()
  }

  def standRoute(json: JsValue): String = {
    val playerId = (json \ "playerId").as[String]

    if (!controller.loadGameManager(playerId)) {
      return Json.obj(
        "success" -> false,
        "msg" -> "No game available"
      ).toString()
    }
    val currentGamestate = controller.gameManager.gameStates(controller.currentPlayerIndex).size
    controller.standCommand(playerId)

    Json.obj(
      "success" -> true,
      "playerCards" -> getAllPlayerCards(),
      "dealerCards" -> getAllDealerCards(),
      "playerCardsValue" -> controller.gameManager.getPlayerHandValue(controller.currentPlayerIndex),
      "dealerCardsValue" -> controller.gameManager.getDealerHandValue,
      "gameStates" -> gameStatesToJsonArray(currentGamestate)
    ).toString()
  }

  private def getAllPlayerCards(): JsArray = {
    var jsArray: JsArray = JsArray()
    for (i <- 0 until controller.gameManager.getPlayerHandSize(controller.currentPlayerIndex)) {
      jsArray = jsArray.append(Json.obj("card" -> controller.gameManager.getPlayerCard(controller.currentPlayerIndex, i).toString))
    }
    jsArray
  }

  private def getAllDealerCards(): JsArray = {
    var jsArray: JsArray = JsArray()
    for (i <- 0 until controller.gameManager.getDealerHandSize) {
      jsArray = jsArray.append(Json.obj("card" -> controller.gameManager.getDealerCard(i).toString))
    }
    jsArray
  }

  private def gameStatesToJsonArray(startIndex: Int) = {
    var playerArray: JsArray = JsArray()

    for (playerIndex <- controller.gameManager.currentPlayerInRound.indices) {
      var jsArray: JsArray = JsArray()
      for (i <- startIndex until controller.gameManager.gameStates(playerIndex).size) {
        jsArray = jsArray.append(Json.obj("gameState" -> controller.gameManager.gameStates(playerIndex)(i).toString))
      }
      playerArray = playerArray.append(jsArray)
    }

    playerArray
  }
}
