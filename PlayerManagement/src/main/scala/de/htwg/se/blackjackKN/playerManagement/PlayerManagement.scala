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
  def main(args: Array[String]) {
    implicit val actorSystem: ActorSystem = ActorSystem("actorSystem")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

    val controller: Controller = new Controller()

    val route = concat(
      path("player" / IntNumber) { id =>
        concat(
          get {
            complete(HttpEntity(ContentTypes.`application/json`, controller.getPlayer(id)))
          },
          path("bet") {
            concat(
              post(
                entity(as[JsValue]) { json => {
                  complete(HttpEntity(ContentTypes.`application/json`, controller.newBet(id, json)))
                }
                }
              )
            )
          }
        )
      },
      path("player") {
        concat(
          post(
            entity(as[JsValue]) { json => {
              complete(HttpEntity(ContentTypes.`application/json`, controller.createNewPlayer(json)))
            }
            }
          )
        )
      }
    )

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => actorSystem.terminate()) // and shutdown when done
  }
}