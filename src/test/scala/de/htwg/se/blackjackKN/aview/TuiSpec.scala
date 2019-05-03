package de.htwg.se.blackjackKN.aview

import de.htwg.se.blackjackKN.controller.Controller
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import de.htwg.se.blackjackKN.model.Card

@RunWith(classOf[JUnitRunner])
class TuiSpec extends WordSpec with Matchers{
  "The Tui" should {
    val controller : Controller = new Controller()
    val tui = new Tui(controller)
    controller.startGame()
    "process n" in {
      tui.processInput("n")
      controller.dealer.drawCard() should be(a [Card])
    }
    "process h" in {
      controller.startNewRound()
      tui.processInput("h")
      controller.output.contains("You hit") should be(true)
    }
    "process s" in {
      controller.startNewRound()
      tui.processInput("s")
      controller.output.contains("You stand") should be(true)
    }
    "process Scala ist toll!" in {
      tui.processInput("Scala ist toll!")

    }
  }
}
