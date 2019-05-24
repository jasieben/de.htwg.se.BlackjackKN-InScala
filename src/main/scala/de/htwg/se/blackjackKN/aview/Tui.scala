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
          output += "You have a " + controller.player.getCard(0) + "\n"
          output += "The dealer has a " + controller.dealer.getCard(0) + "\n"
          output += "You also have a " + controller.player.getCard(1) + "\n"
          output += "The combined value of your cards is " + controller.player.getHandValue +"\n"
        case GameState.STAND =>
          output += "You stand\n"
        case GameState.HIT =>
          output += "You hit and draw a " +  controller.player.getLastHandCard + "\n"
          output += "The combined value of your cards is " + controller.player.getHandValue + "\n"
        case GameState.REVEAL =>
          output += "The dealer has a " + controller.dealer.getCard(1) + "\n"
        case GameState.DEALER_DRAWS =>
          for (i <- 2 until controller.dealer.getHandSize) {
            output += "The dealer draws a " + controller.dealer.getCard(i) + "\n"
          }
          output += "The dealers combined value of cards is " + controller.dealer.getHandValue + "\n"
        case GameState.PLAYER_BUST =>
          output += "You bust!\n"
        case GameState.DEALER_BUST =>
          output += "The dealer busts, you win!\n"
        case GameState.PLAYER_BLACKJACK =>
          output += "You have a blackjack!\n"
        case GameState.WAITING_FOR_INPUT =>
          output += "Would you like to hit(h) or stand(s)?"
        case GameState.PLAYER_WINS =>
          output += "You win!\nYou won " + controller.player.bet.value + "$!\n"
          output += "Your new balance is " + controller.player.balance + "$\n"
        case GameState.PLAYER_LOOSE =>
          output += "You loose!\n"
          output += "Your current balance is " + controller.player.balance + "$\n"
        case GameState.PUSH =>
          output += "Push! You and the dealer have a combined card value of " + controller.dealer.getHandValue + "\n"
          output += "Your balance stays at " + controller.player.balance + "$\n"
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
      }
    }
    print()
    true
  }

  def processInput(input: String): Unit = {
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
        println("Your current balance is " + controller.player.balance + "$")
      case betRegEx(_*) =>
        val number = input.replaceAll("n ", "")
        controller.setBet(number.toInt)
        controller.startNewRound()
      case _ =>
        println("Input not recognized!")
    }
  }
  def print() : Unit = {
    println(output)
    controller.clearGameStates()
  }
}
