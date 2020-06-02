package de.htwg.se.blackjackKN.gamelogic

import com.google.inject.Guice
import de.htwg.se.blackjackKN.gamelogic.aview.Tui
import de.htwg.se.blackjackKN.gamelogic.aview.gui.Gui
import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.ControllerInterface

import scala.io.StdIn.readLine

object BlackjackKN {
  def main(args: Array[String]): Unit = {
    val injector = Guice.createInjector(new BlackjackModule)
    val controller = injector.getInstance(classOf[ControllerInterface])
    val tui = new Tui(controller)

    if (args.length < 2 && !args.contains("test")) {
      //val gui = new Gui(controller)
      //new Thread(() => {
      //  gui.main(Array())
      //}).start()
    }

    controller.startGame()
    var input: String = ""
    if (!args.isEmpty) tui.processInput(args(0))
    else do {
      input = readLine()
      tui.processInput(input)
    } while (input != "exit")
  }
}