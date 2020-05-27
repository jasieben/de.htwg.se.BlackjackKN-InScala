package de.htwg.se.blackjackKN.model

import de.htwg.se.blackjackKN.model.cardsComponent.cardsBaseImpl.{FaceCard, NumberCard}
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DealerSpec extends WordSpec with Matchers {
  "A Dealer" when {
    var dealer = Dealer()
    "new" should {
      "have a name" in {
        dealer.name should be("Dealer")
      }
      "have a nice String representation" in {
        dealer.toString should be("Dealer")
      }
    }
    "generating Cards" should {
      "give the right amount of cards" in {
        dealer = dealer.generateDealerCards
        dealer.cardDeck.size should be(260)
      }
    }
    "drawing Card" should {
      "be a card" in {
        val ogSize = dealer.cardDeck.size
        dealer.drawCard() should (be(a[NumberCard]) or be(a[FaceCard]))
        dealer = dealer.dropCard()
        dealer.cardDeck.size should be(ogSize - 1)
      }
    }

    "calling" should {
      "getLastHandCard()" in {
        dealer = dealer.generateDealerCards
        dealer = dealer.addCardToHand(dealer.drawCard())
        dealer = dealer.dropCard()
        val testCard = dealer.drawCard()
        dealer = dealer.addCardToHand(testCard)
        dealer = dealer.dropCard()
        dealer.getLastHandCard should be(testCard)
      }
      "containsCardType when containing card" in {
        dealer = dealer.generateDealerCards
        dealer = dealer.clearHand()
        dealer = dealer.addCardToHand(FaceCard(Suits.Hearts, Ranks.Jack))
        dealer = dealer.addCardToHand(FaceCard(Suits.Spades,Ranks.Ace))
        dealer.containsCardType(Ranks.Ace) should be(1)
      }
      "containsCardType when not containing card" in {
        dealer.generateDealerCards
        dealer.clearHand()
        dealer.addCardToHand(FaceCard(Suits.Hearts,Ranks.Ace))
        dealer.addCardToHand(FaceCard(Suits.Spades,Ranks.Ace))
        dealer.containsCardType(Ranks.King) should be(-1)
      }
    }
  }
}
