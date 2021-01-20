package de.htwg.se.blackjackKN.gamelogic.aview

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, HttpResponse}
import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.{ControllerInterface, GameState}
import de.htwg.se.blackjackKN.gamelogic.util.Observer
import play.api.libs.json.Json

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}


class Tui(controller: ControllerInterface) extends Observer {

  //controller.add(this)
  var gamestatePointer: Int = 0
  var output: String = ""
  var firstAceMessage = false

  implicit val actorSystem: ActorSystem = ActorSystem("actorSystemTui")
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

  //Show Splashscreen
  output = "\n.------..------..------..------..------..------..------..------..------.\n|B.--. ||L.--. ||A.--. |" +
    "|C.--. ||K.--. ||J.--. ||A.--. ||C.--. ||K.--. |\n| :(): || :/\\: || (\\/) || :/\\: || :/\\: || :(): || (\\" +
    "/) || :/\\: || :/\\: |\n| ()() || (__) || :\\/: || :\\/: || :\\/: || ()() || :\\/: || :\\/: || :\\/: |\n| '" +
    "--'B|| '--'L|| '--'A|| '--'C|| '--'K|| '--'J|| '--'A|| '--'C|| '--'K|\n`------'`------'`------'`------'`---" +
    "---'`------'`------'`------'`------'\n"
  output += "Create a new Player by entering \"create (Your Name)\"\n"
  output += "Enter n *dollars* to set a bet and start a new game!\n"
  print()

  override def update: Boolean = {
    output = ""
    var counter: Int = 0
    for (i <- gamestatePointer until controller.gameManager.gameStates.length) {
      controller.gameManager.gameStates(i) match {
        case GameState.IDLE =>
          output += "Press n to start a new game!\n"
        case GameState.SHUFFLING =>
          output += "Card Deck is being changed and shuffled" + "\n"
        case GameState.FIRST_ROUND =>
          output += "Player" + " has a " + controller.gameManager.getPlayerCard(0, 0) + "\n"
          output += "The dealer has a " + controller.gameManager.getDealerCard(0) + "\n"
          output += "Player" + " also has a " + controller.gameManager.getPlayerCard(0, 1) + "\n"
          output += "The combined value of your cards is " + controller.gameManager.getPlayerHandValue(0) + "\n"
        case GameState.STAND =>
          output += "Player" + " stands\n"
        case GameState.HIT =>
          output += "Player" + " hits and draws a " + controller.gameManager.getLastPlayerHandCard(0) + "\n"
          output += "The combined value of " + "Player" + "s cards are " + controller.gameManager.getPlayerHandValue(0) + "\n"
        case GameState.REVEAL =>
          output += "The dealer has a " + controller.gameManager.getDealerCard(1) + "\n"
        case GameState.DEALER_DRAWS =>
          for (i <- 2 until controller.gameManager.getDealerHandSize) {
            output += "The dealer draws a " + controller.gameManager.getDealerCard(i) + "\n"
          }
          output += "The dealers combined value of cards is " + controller.gameManager.getDealerHandValue + "\n"
        case GameState.PLAYER_BUST =>
          output += "Player" + " busts!\n"
        case GameState.DEALER_BUST =>
          output += "The dealer busts, " + "Player" + " wins!\n"
        case GameState.PLAYER_BLACKJACK =>
          output += "Player" + " has a blackjack!\n"
        case GameState.WAITING_FOR_INPUT =>
          output += "Would you like to hit(h) or stand(s)?"
        case GameState.PLAYER_WINS =>
          // TODO:
          output += "Player" + " wins!\n" + "Player" + " won!\n"
        //output += "Player" + "s new balance is " + controller.player.balance + "$\n"
        case GameState.PLAYER_LOOSE =>
          // TODO:
          output += "Player" + " looses!\n"
        //output += "Player" + "s current balance is " + controller.player.balance + "$\n"
        case GameState.PUSH =>
          // TODO:
          output += "Push! " + "Player" + " and the dealer have a combined card value of " + controller.gameManager.getDealerHandValue + "\n"
        //output += "Player" + "s balance stays at " + controller.player.balance + "$\n"
        case GameState.ACE =>
          if (!firstAceMessage && !controller.gameManager.gameStates.contains(GameState.PLAYER_BLACKJACK))
            output += "or your cards can value " + (controller.gameManager.getPlayerHandValue(0) - 10) + "\n"
          firstAceMessage = true
        case GameState.BET_FAILED =>
          output += "Bet failed. Not enough money or no bet set"
        case GameState.BET_SET =>
          output += "Bet set"
        case GameState.UNDO =>
          output += "Undo last input operation\n"
        case GameState.REDO =>
          output += "Redo last input operation\n"
        case GameState.DEALER_BLACKJACK =>
          output += "The dealer has a Blackjack!\n"
      }
      counter = i
    }
    gamestatePointer = counter + 1
    print()
    true
  }

  def processInput(input: String): Unit = {
    val createNameRegEx = "create \\w+".r
    val betRegEx = "n \\d+".r
    input match {
      case "Scala ist toll!" =>
        println("Sag mir was neues\n (☞ﾟヮﾟ)☞ ")
      case "h" =>
        controller.hitCommand("123")
      case "s" =>
        controller.standCommand("123")
      case "z" =>
        controller.undo()
      case "y" =>
        controller.redo()
      case "b" =>
        // TODO: Make request to player management
        //output = "Your current balance is " + controller.player.balance + "$"
        print()
      case createNameRegEx(_*) =>
        val name = input.replaceAll("create ", "")
        val json = Json.obj(
          "name" -> name
        ).toString()
        val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(HttpMethods.POST, uri = controller.playerManagementServiceUrl + "player", entity = HttpEntity.apply(json)))
        Await.result(responseFuture, Duration("5s"))
        responseFuture.onComplete {
          case Success(res) => {
            output = "Player name is set to " + name + "\n"
            print()
          }
          case Failure(_) => sys.error("Could not resolve bet")
        }
      case betRegEx(_*) =>
        val number = input.replaceAll("n ", "")
        controller.setBet("123" ,number.toInt)
      case "exit" =>
        output = "Exiting game..."
        print()
      case _ =>
        println("Input not recognized!")
    }
  }

  def print(): Unit = {
    println(output)

  }
}
