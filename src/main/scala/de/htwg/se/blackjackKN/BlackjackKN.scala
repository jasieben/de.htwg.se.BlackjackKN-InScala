package de.htwg.se.blackjackKN

import de.htwg.se.blackjackKN.aview.Tui
import de.htwg.se.blackjackKN.controller.Controller

import scala.io.StdIn.readLine

object BlackjackKN {
  def main(args: Array[String]): Unit = {

    val controller : Controller = new Controller
    val tui = new Tui(controller)
    controller.startGame()
    var input: String = ""
    if (!args.isEmpty) tui.processInput(args(0))
    else do {
      input = readLine()
      tui.processInput(input)
    } while (input != "exit")
  }
}