package de.htwg.se.blackjackKN.gamelogic.model

import de.htwg.se.blackjackKN.model.cardsComponent.cardsBaseImpl.{CardDeck, FaceCard, NumberCard}
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
        testCard.toString should be("King of Diamonds")
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
      "get correct image filename" in {
        val numbersTestCardClubs = NumberCard(Suits.Clubs,9)
        val numbersTestCardDiamonds = NumberCard(Suits.Diamonds,9)
        val numbersTestCardSpades = NumberCard(Suits.Spades,9)
        numbersTestCardClubs.getBackgroundImageFileName should be("9C.png")
        numbersTestCardDiamonds.getBackgroundImageFileName should be("9D.png")
        numbersTestCardSpades.getBackgroundImageFileName should be("9S.png")
        numbersTestCard.getBackgroundImageFileName should be("9H.png")

        val faceTestCardKing = FaceCard(Suits.Clubs, Ranks.King)
        val faceTestCardJack = FaceCard(Suits.Diamonds, Ranks.Jack)
        val faceTestCardSQueen = FaceCard(Suits.Spades,Ranks.Queen)
        val faceTestCardSAce = FaceCard(Suits.Spades,Ranks.Ace)

        faceTestCardKing.getBackgroundImageFileName should be("KC.png")
        faceTestCardJack.getBackgroundImageFileName should be("JD.png")
        faceTestCardSQueen.getBackgroundImageFileName should be("QS.png")
        faceTestCardSAce.getBackgroundImageFileName should be("AS.png")
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

