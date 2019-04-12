/*package de.htwg.se.blackjackKN.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CardSpec extends WordSpec with Matchers {
  "A face card" when {
    "new" should {
      val testCard = Card("spades", 10, "king")
      "have a suit" in {
        testCard.suit should be("spades")
      }
      "have a value" in {
        testCard.value should be(10)
      }
      "have a rank" in {
        testCard.rank should be("king")
      }
      "have a nice String representation" in {
        testCard.toString should be("king of spades")
      }
    }
  }
  "Another numbers card" when {
    "new" should {
      val numbersTestCard = Card("hearts", 9, "9")
      "have a suit" in {
        numbersTestCard.suit should be("hearts")
      }
      "have a value" in {
        numbersTestCard.value should be(9)
      }
      "have a rank" in {
        numbersTestCard.rank should be("9")
      }
      "have corresponding values" in {
        numbersTestCard.rank.toInt should be(numbersTestCard.value)
      }
      "have a nice String representation" in {
        numbersTestCard.toString should be("9 of hearts")
      }
    }
  }
}*/

