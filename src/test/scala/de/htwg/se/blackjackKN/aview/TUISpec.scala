package de.htwg.se.blackjackKN.aview

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TUISpec extends WordSpec with Matchers {
  "Exit the Game" when {
    def processInput(input: String): Unit = "exit"
    "when enter exit" should {
      System.exit(0)
    }
  }
}
