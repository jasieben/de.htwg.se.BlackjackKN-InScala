package de.htwg.se.blackjackKN.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CardSpec extends WordSpec with Matchers {
  "A face card" when {
    val testCard = FaceCard("diamonds", "king")
    "new" should {
      "have a suit" in {
        testCard.suit should be("diamonds")
      }
      "have a value" in {
        testCard.value should be(10)
      }
      "have a rank" in {
        testCard.rank should be("king")
      }
    }
  }
  "Another numbers card" when {
    val numbersTestCard = NumberCard("hearts","9")
    "new" should {
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
    }
  }
  "A CardDeck" when {
    val cardDeck = CardDeck().cardDeck
    "new" should {
      "be a List of Cards" in {
        cardDeck should be (a [List[Card]])
      }
    }
  }
}

