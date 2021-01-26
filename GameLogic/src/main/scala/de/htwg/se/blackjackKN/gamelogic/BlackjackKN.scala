package de.htwg.se.blackjackKN.gamelogic

import com.google.inject.Guice
import de.htwg.se.blackjackKN.gamelogic.aview.{RestApi, Tui}
import de.htwg.se.blackjackKN.gamelogic.aview.gui.Gui
import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.ControllerInterface

import scala.io.StdIn
import scala.io.StdIn.readLine

object BlackjackKN {
  def main(args: Array[String]): Unit = {
    val injector = Guice.createInjector(new BlackjackModule)
    val controller = injector.getInstance(classOf[ControllerInterface])
    val tui = new Tui(controller)
    val restApi = new RestApi(controller)

    if (args.length < 2 && !args.contains("test")) {
      //val gui = new Gui(controller)
      //new Thread(() => {
      //  gui.main(Array())
      //}).start()
    }

    new Thread(() => {
      restApi.run()
    }).start()

    controller.startGame()
    StdIn.readLine()
  }
}