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
          path("start") {
            concat(
              put(
                entity(as[String]) { jsonString => {
                  val json = Json.parse(jsonString)
                  complete(HttpEntity(ContentTypes.`application/json`, newGameRoute(json)))
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

  private def newGameRoute(json: JsValue): String = {
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
    val currentGamestate = controller.gameManager.gameStates.size
    controller.setBet(playerId, betValue)
    if (controller.gameManager.gameStates.isEmpty || controller.gameManager.gameStates.last == GameState.BET_FAILED) {

      return Json.obj(
        "success" -> false,
        "msg" -> "Failed to set bet.",
        "gameStates" -> gameStatesToJsonArray(currentGamestate)
      ).toString()
    }

    var revealed = false

    if (controller.gameManager.gameStates.exists(p => p.equals(GameState.REVEAL) || p.equals(GameState.PLAYER_BUST) || p.equals(GameState.DEALER_BUST))) {
      revealed = true
    }

    Json.obj(
      "playerCards" -> getAllPlayerCards(),
      "dealerCards" -> (if (revealed) getAllDealerCards() else Json.arr()),
      "playerCardsValue" -> controller.gameManager.getPlayerHandValue(0),
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

    val currentGamestate = controller.gameManager.gameStates.size
    controller.hitCommand(playerId)

    var revealed = false

    if (controller.gameManager.gameStates.exists(p => p.equals(GameState.REVEAL) || p.equals(GameState.PLAYER_BUST) || p.equals(GameState.DEALER_BUST))) {
      revealed = true
    }

    Json.obj(
      "success" -> true,
      "hitCard" -> controller.gameManager.getPlayerCard(0, controller.gameManager.getPlayerHandSize(0) - 1).toString,
      "dealerCards" -> (if (revealed) getAllDealerCards() else Json.arr()),
      "playerCardsValue" -> controller.gameManager.getPlayerHandValue(0),
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
    val currentGamestate = controller.gameManager.gameStates.size
    controller.standCommand(playerId)

    Json.obj(
      "success" -> true,
      "playerCards" -> getAllPlayerCards(),
      "dealerCards" -> getAllDealerCards(),
      "playerCardsValue" -> controller.gameManager.getPlayerHandValue(0),
      "dealerCardsValue" -> controller.gameManager.getDealerHandValue,
      "gameStates" -> gameStatesToJsonArray(currentGamestate)
    ).toString()
  }

  private def getAllPlayerCards(): JsArray = {
    var jsArray: JsArray = JsArray()
    for (i <- 0 until controller.gameManager.getPlayerHandSize(0)) {
      jsArray = jsArray.append(Json.obj("card" -> controller.gameManager.getPlayerCard(0, i).toString))
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

  private def gameStatesToJsonArray(startIndex: Int): JsArray = {
    var jsArray: JsArray = JsArray()
    for (i <- startIndex until controller.gameManager.gameStates.size) {
      jsArray = jsArray.append(Json.obj("gameState" -> controller.gameManager.gameStates(i).toString))
    }
    jsArray
  }
}
