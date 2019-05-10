package de.htwg.se.blackjackKN.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CardSpec extends WordSpec with Matchers {
  "A face card" when {
    val testCard = FaceCard(Suits.Diamonds, Ranks.King)
    "new" should {
      "have a suit" in {
        testCard.suit should be(Suits.Diamonds)
      }
      "have a value" in {
        testCard.value should be(10)
      }
      "have a rank" in {
        testCard.rank should be(Ranks.King)
      }
      "have a nice String representation" in {
        testCard.toString should be("King of diamonds")
      }
    }
  }
  "Another numbers card" when {
    val numbersTestCard = NumberCard(Suits.Hearts,9)
    "new" should {
      "have a suit" in {
        numbersTestCard.suit should be(Suits.Hearts)
      }
      "have a value" in {
        numbersTestCard.value should be(9)
      }
      "have a rank" in {
        numbersTestCard.rank should be(9)
      }
      "have corresponding values" in {
        numbersTestCard.rank should be(numbersTestCard.value)
      }
    }
  }
  "A CardDeck" when {
    val cardDeck = CardDeck()
    "new" should {
      "be a List of Cards" in {
        cardDeck.cardDeck should be (a [List[_]])
      }
      "a String representation" in {
        cardDeck.toString should be("CardDeck has 52 cards")
      }
    }
  }
}

