package de.htwg.se.blackjackKN.model

import org.scalatest._
  import org.junit.runner.RunWith
  import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DealerSpec extends WordSpec with Matchers {
  "A Dealer" when {
    val dealer = Dealer()
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
        dealer.generateDealerCards.size should be(312)
      }
    }
    "drawing Card" should {
      "be a card" in {
        dealer.drawCard() should (be(a[NumberCard]) or be(a[FaceCard]))
      }
    }

    "calling" should {
      "getLastHandCard()" in {
        dealer.generateDealerCards
        dealer.addCardToHand(dealer.drawCard())
        val testCard = dealer.addCardToHand(dealer.drawCard())
        dealer.getLastHandCard should be(testCard)
      }
      "containsCardType when containing card" in {
        dealer.generateDealerCards
        dealer.clearHand()
        dealer.addCardToHand(FaceCard(Suits.Hearts, Ranks.Jack))
        dealer.addCardToHand(FaceCard(Suits.Spades,Ranks.Ace))
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
