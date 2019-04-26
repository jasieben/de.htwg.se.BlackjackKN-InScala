package de.htwg.se.blackjackKN.aview

import de.htwg.se.blackjackKN.controller.Controller
import de.htwg.se.blackjackKN.util.Observer


class Tui (controller : Controller) extends Observer {
  def update : Unit = println(controller.display)
  controller.add(this)
  controller.startGame()


  def processInput(input: String): Unit = {
    input match {
      case "n" =>
        controller.startNewRound()
      case "exit" =>
        println("Exiting Blackjack...")
        System.exit(0)
      case "Scala ist toll!" =>
        println("Sag mir was neues\n (☞ﾟヮﾟ)☞ ")
      case "h" =>
        controller.hit()
      case "s" =>
        controller.stand()
      case _ =>
        println("Input not recognized!")
    }
  }
}
