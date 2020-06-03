package de.htwg.se.blackjackKN.playerManagement

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream
import akka.stream.{ActorMaterializer, Materializer}
import de.htwg.se.blackjackKN.playerManagement.controller.Controller
import de.htwg.se.blackjackKN.playerManagement.model.Player
import play.api.libs.json.Json
import spray.json.JsValue

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object PlayerManagement {
  val connectionInterface = "0.0.0.0"
  val connectionPort = 1274

  def main(args: Array[String]) {
    implicit val actorSystem: ActorSystem = ActorSystem("actorSystem")
    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

    val controller: Controller = new Controller()

    val route = concat(
      pathPrefix("player" / IntNumber) { id =>
        concat(
          get {
            complete(HttpEntity(ContentTypes.`application/json`, controller.getPlayer(id)))
          },
          path("bet") {
            concat(
              post(
                entity(as[String]) { jsonString => {
                  val json = Json.parse(jsonString)
                  complete(HttpEntity(ContentTypes.`application/json`, controller.newBet(id, json)))
                }
                }
              )
            )
          },
          path("bet" / "resolve") {
            concat(
              put(
                entity(as[String]) { jsonString => {
                  val json = Json.parse(jsonString)
                  complete(HttpEntity(ContentTypes.`application/json`, controller.resolveBet(id, json)))
                }
                }
              )
            )
          }
        )
      },
      pathPrefix("player") {
        concat(
          post(
            entity(as[String]) { jsonString => {
              val json = Json.parse(jsonString)
              complete(HttpEntity(ContentTypes.`application/json`, controller.createNewPlayer(json)))
            }
            }
          )
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
}