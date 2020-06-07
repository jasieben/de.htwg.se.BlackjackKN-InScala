package de.htwg.se.blackjackKN.gamelogic

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BlackJackKNSpec extends WordSpec with Matchers {
  "The Blackjack main class" should {
    "accept a string" in {
      BlackjackKN.main(Array[String]("Test-String", "test"))
    }
  }
}
