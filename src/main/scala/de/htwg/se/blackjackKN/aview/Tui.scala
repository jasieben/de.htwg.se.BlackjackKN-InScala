package de.htwg.se.blackjackKN.aview

import de.htwg.se.blackjackKN.controller.{Controller, GameState}
import de.htwg.se.blackjackKN.util.Observer


class Tui (controller : Controller) extends Observer {

  controller.add(this)
  var output : String = ""
  var firstAceMessage = false

  //Show Splashscreen
  output = "\n.------..------..------..------..------..------..------..------..------.\n|B.--. ||L.--. ||A.--. |" +
    "|C.--. ||K.--. ||J.--. ||A.--. ||C.--. ||K.--. |\n| :(): || :/\\: || (\\/) || :/\\: || :/\\: || :(): || (\\" +
    "/) || :/\\: || :/\\: |\n| ()() || (__) || :\\/: || :\\/: || :\\/: || ()() || :\\/: || :\\/: || :\\/: |\n| '" +
    "--'B|| '--'L|| '--'A|| '--'C|| '--'K|| '--'J|| '--'A|| '--'C|| '--'K|\n`------'`------'`------'`------'`---" +
    "---'`------'`------'`------'`------'\n"
  output += "Create a new Player by entering \"create (Your Name)\"\n"
  output += "Enter n *dollars* to set a bet and start a new game!\n"
  output += "Your current balance is " + controller.player.balance + "$"
  print()

  override def update : Boolean = {
    output = ""
    for (gameState <- controller.gameStates) {
      gameState match {
        case GameState.IDLE =>
          output += "Press n to start a new game!\n"
        case GameState.SHUFFLING =>
          output += "Card Deck is being changed and shuffled" + "\n"
        case GameState.FIRST_ROUND =>
          output += controller.player.name + " has a " + controller.player.getCard(0) + "\n"
          output += "The dealer has a " + controller.dealer.getCard(0) + "\n"
          output += controller.player.name + " also has a " + controller.player.getCard(1) + "\n"
          output += "The combined value of your cards is " + controller.player.getHandValue +"\n"
        case GameState.STAND =>
          output += controller.player.name + " stands\n"
        case GameState.HIT =>
          output += controller.player.name + " hits and draws a " +  controller.player.getLastHandCard + "\n"
          output += "The combined value of " + controller.player.name +"s cards are " + controller.player.getHandValue + "\n"
        case GameState.REVEAL =>
          output += "The dealer has a " + controller.dealer.getCard(1) + "\n"
        case GameState.DEALER_DRAWS =>
          for (i <- 2 until controller.dealer.getHandSize) {
            output += "The dealer draws a " + controller.dealer.getCard(i) + "\n"
          }
          output += "The dealers combined value of cards is " + controller.dealer.getHandValue + "\n"
        case GameState.PLAYER_BUST =>
          output += controller.player.name + " busts!\n"
        case GameState.DEALER_BUST =>
          output += "The dealer busts, " + controller.player.name + " wins!\n"
        case GameState.PLAYER_BLACKJACK =>
          output += controller.player.name + " has a blackjack!\n"
        case GameState.WAITING_FOR_INPUT =>
          output += "Would you like to hit(h) or stand(s)?"
        case GameState.PLAYER_WINS =>
          output += controller.player.name + " wins!\n" + controller.player.name + " won " + controller.player.bet.value + "$!\n"
          output += controller.player.name + "s new balance is " + controller.player.balance + "$\n"
        case GameState.PLAYER_LOOSE =>
          output += controller.player.name + " looses!\n"
          output += controller.player.name + "s current balance is " + controller.player.balance + "$\n"
        case GameState.PUSH =>
          output += "Push! " + controller.player.name + " and the dealer have a combined card value of " + controller.dealer.getHandValue + "\n"
          output += controller.player.name + "s balance stays at " + controller.player.balance + "$\n"
        case GameState.ACE =>
          if (!firstAceMessage && !controller.gameStates.contains(GameState.PLAYER_BLACKJACK))
            output += "or your cards can value " + (controller.player.getHandValue - 10) + "\n"
            firstAceMessage = true
        case GameState.BET_FAILED =>
          output += "Bet failed. Not enough money or no bet set"
        case GameState.BET_SET =>
          output += "Bet of " + controller.player.bet.value + "$ set"
        case GameState.UNDO =>
          output += "Undo last input operation\n"
        case GameState.REDO =>
          output += "Redo last input operation\n"
        case GameState.NEW_NAME =>
          output += "Player name is set to " + controller.player.name + "\n"
        case GameState.DEALER_BLACKJACK =>
          output += "The dealer has a Blackjack!\n"
      }
    }
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
        controller.hitCommand()
      case "s" =>
        controller.standCommand()
      case "z" =>
        controller.undo()
      case "y" =>
        controller.redo()
      case "b" =>
        output = "Your current balance is " + controller.player.balance + "$"
        print()
      case createNameRegEx(_*) =>
        val name = input.replaceAll("create ", "")
        controller.createNewPlayer(name)
      case betRegEx(_*) =>
        val number = input.replaceAll("n ", "")
        controller.setBet(number.toInt)
        controller.startNewRound()
      case "exit" =>
        output = "Exiting game..."
        print()
      case _ =>
        println("Input not recognized!")
    }
  }
  def print() : Unit = {
    println(output)
    controller.clearGameStates()
  }
}
