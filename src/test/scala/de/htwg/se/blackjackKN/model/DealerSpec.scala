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
        dealer.drawCard() should(be(a [NumberCard]) or be( a [FaceCard]))
      }
    }
  }
}
