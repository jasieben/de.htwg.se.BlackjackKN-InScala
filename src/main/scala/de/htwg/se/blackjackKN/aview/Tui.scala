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
    "---'`------'`------'`------'`------'"

  print()

  override def update : Boolean = {
    output = ""
    for (gameState <- controller.gameStates) {
      gameState match {
        case GameState.IDLE =>
          output += "\nPress n to start a new game!"
        case GameState.SHUFFLING =>
          output += "\nCard Deck is being changed and shuffled"
        case GameState.FIRST_ROUND =>
          output += "You have a " + controller.player.getCard(0)
          output += "\nThe dealer has a " + controller.dealer.getCard(0)
          output += "\nYou also have a " + controller.player.getCard(1)
          output += "\nThe combined value of your cards is " + controller.player.getHandValue
        case GameState.STAND =>
          output += "You stand"
        case GameState.HIT =>
          output += "You hit and draw a " +  controller.player.getLastHandCard
          output += "\nThe combined value of your cards is " + controller.player.getHandValue
        case GameState.REVEAL =>
          output += "\nThe dealer has a " + controller.dealer.getCard(1)
        case GameState.DEALER_DRAWS =>
          for (i <- 2 until controller.dealer.getHandSize) {
            output += "\nThe dealer draws a " + controller.dealer.getCard(i)
          }
          output += "\nThe dealers combined value of cards is " + controller.dealer.getHandValue
        case GameState.PLAYER_BUST =>
          output += "\nYou bust!"
        case GameState.DEALER_BUST =>
          output += "\nThe dealer busts, you win!"
        case GameState.PLAYER_BLACKJACK =>
          output += "\nYou have a blackjack!"
        case GameState.WAITING_FOR_INPUT =>
          output += "\nWould you like to hit(h) or stand(s)?"
        case GameState.PLAYER_WINS =>
          output += "\nYou win!"
        case GameState.PLAYER_LOOSE =>
          output += "\nYou loose!"
        case GameState.PUSH =>
          output += "\nPush! You and the dealer have a combined card value of " + controller.dealer.getHandValue
        case GameState.ACE =>
          if (!firstAceMessage)
            output += "\nor your cards can value " + (controller.player.getHandValue - 10)
            firstAceMessage = true
        case GameState.BET_FAILED =>
          output += "\nBet failed. Not enough money!"
        case GameState.BET_SET => {
          output += "\nBet of " + controller.player.bet.value + "$ set"
        }
      }
    }
    print()
    true
  }

  def processInput(input: String): Unit = {
    val betRegEx = "bet \\d+".r
    input match {
      case "n" =>
        controller.startNewRound()
      case "Scala ist toll!" =>
        println("Sag mir was neues\n (☞ﾟヮﾟ)☞ ")
      case "h" =>
        controller.hit()
      case "s" =>
        controller.stand()
      case betRegEx(_*) =>
        val intPattern = "\\d+".r
        val intPattern(value) = input
        controller.setBet(value.toInt)

      case _ =>
        println("Input not recognized!")
    }
  }
  def print() : Unit = {
    println(output)
    controller.clearGameStates()
  }
}
