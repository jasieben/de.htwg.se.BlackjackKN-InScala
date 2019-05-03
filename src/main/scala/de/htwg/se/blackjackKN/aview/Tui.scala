package de.htwg.se.blackjackKN.aview

import de.htwg.se.blackjackKN.controller.Controller
import de.htwg.se.blackjackKN.util.Observer


class Tui (controller : Controller) extends Observer {
  override def update : Boolean = {println(controller.display); true}
  controller.add(this)


  def processInput(input: String): Unit = {
    input match {
      case "n" =>
        controller.startNewRound()
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
