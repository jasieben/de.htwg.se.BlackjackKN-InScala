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
  val connectionInterface = "localhost"
  val connectionPort = 5000

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
    val playerId = (json \ "playerId").as[Int]
    val betValue = (json \ "betValue").as[Int]
    val currentGamestate = controller.gameStates.size
    controller.setBet(betValue)
    controller.startNewRound()

    val dealerCardsArray = Json.arr(Json.obj("card" -> controller.gameManager.getDealerCard(0).getCardId))
    var revealed = false

    if (controller.gameStates.exists(p => p.equals(GameState.REVEAL))) {
      revealed = true
    }

    Json.obj(
      "playerCards" -> getAllPlayerCards(),
      "dealerCards" -> (if (revealed) getAllDealerCards() else dealerCardsArray),
      "playerCardsValue" -> controller.gameManager.getPlayerHandValue(0),
      "dealerCardsValue" -> controller.gameManager.getDealerHandValue,
      "gameStates" -> gameStatesToJsonArray(currentGamestate)
    ).toString()
  }

  def hitRoute(json: JsValue): String = {
    val playerId = (json \ "playerId").as[Int]

    val currentGamestate = controller.gameStates.size
    controller.hitCommand()

    val dealerCardsArray = Json.arr(Json.obj("card" -> controller.gameManager.getDealerCard(0).getCardId))
    var revealed = false

    if (controller.gameStates.exists(p => p.equals(GameState.REVEAL))) {
      revealed = true
    }

    Json.obj(
      "hitCard" -> controller.gameManager.getPlayerCard(0, controller.gameManager.getPlayerHandSize(0) - 1).getCardId,
      "dealerCards" -> (if (revealed) getAllDealerCards() else dealerCardsArray),
      "playerCardsValue" -> controller.gameManager.getPlayerHandValue(0),
      "dealerCardsValue" -> controller.gameManager.getDealerHandValue,
      "gameStates" -> gameStatesToJsonArray(currentGamestate)
    ).toString()
  }

  def standRoute(json: JsValue) : String = {
    val playerId = (json \ "playerId").as[Int]

    val currentGamestate = controller.gameStates.size
    controller.standCommand()

    var revealed = false

    if (controller.gameStates.exists(p => p.equals(GameState.REVEAL))) {
      revealed = true
    }

    Json.obj(
      "playerCards" -> getAllPlayerCards(),
      "dealerCards" -> getAllDealerCards(),
      "playerCardsValue" -> controller.gameManager.getPlayerHandValue(0),
      "dealerCardsValue" -> controller.gameManager.getDealerHandValue,
      "gameStates" -> gameStatesToJsonArray(currentGamestate)
    ).toString()
  }

  private def getAllPlayerCards(): JsArray = {
    var jsArray: JsArray = JsArray()
    for (i <- 0 until controller.gameManager.getPlayerHandSize(0) - 1) {
      jsArray = jsArray.append(Json.obj("card" -> controller.gameManager.getPlayerCard(0, i).getCardId))
    }
    jsArray
  }

  private def getAllDealerCards(): JsArray = {
    var jsArray: JsArray = JsArray()
    for (i <- 0 until controller.gameManager.getPlayerHandSize(0) - 1) {
      jsArray = jsArray.append(Json.obj("card" -> controller.gameManager.getDealerCard(i).getCardId))
    }
    jsArray
  }

  private def gameStatesToJsonArray(startIndex: Int): JsArray = {
    var jsArray: JsArray = JsArray()
    for (i <- startIndex until controller.gameStates.size - 1) {
      jsArray = jsArray.append(Json.obj("gameState" -> controller.gameStates(i).toString))
    }
    jsArray
  }
}
