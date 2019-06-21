package de.htwg.se.blackjackKN

import de.htwg.se.blackjackKN.aview.Tui
import de.htwg.se.blackjackKN.aview.gui.Gui
import de.htwg.se.blackjackKN.controller.ControllerBaseImpl.Controller
import de.htwg.se.blackjackKN.controller.ControllerInterface

import scala.io.StdIn.readLine

object BlackjackKN {
  def main(args: Array[String]): Unit = {

    val controller = new Controller
    val tui = new Tui(controller)
    val gui = new Gui(controller)
    new Thread(() => {
      gui.main(Array())
    }).start()
    controller.startGame()
    var input: String = ""
    if (!args.isEmpty) tui.processInput(args(0))
    else do {
      input = readLine()
      tui.processInput(input)
    } while (input != "exit")
  }
}