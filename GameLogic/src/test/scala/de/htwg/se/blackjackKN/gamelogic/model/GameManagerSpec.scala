package de.htwg.se.blackjackKN.gamelogic.model

import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.cardsBaseImpl.{FaceCard, NumberCard}
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GameManagerSpec extends WordSpec with Matchers {
  "A Dealer" when {
    var gameManager = GameManager(None)
    "generating Cards" should {
      "give the right amount of cards" in {
        gameManager = gameManager.generateDealerCards
        gameManager.cardDeck.size should be(260)
      }
    }
    "drawing Card" should {
      "be a card" in {
        val ogSize = gameManager.cardDeck.size
        gameManager.drawCard() should (be(a[NumberCard]) or be(a[FaceCard]))
        gameManager = gameManager.dropCard()
        gameManager.cardDeck.size should be(ogSize - 1)
      }
    }

    "calling" should {
      "getLastDealerHandCard()" in {
        gameManager = gameManager.generateDealerCards
        gameManager = gameManager.addCardToDealerHand(gameManager.drawCard())
        gameManager = gameManager.dropCard()
        val testCard = gameManager.drawCard()
        gameManager = gameManager.addCardToDealerHand(testCard)
        gameManager = gameManager.dropCard()
        gameManager.getLastDealerHandCard should be(testCard)
      }
      "containsCardType when containing card" in {
        gameManager = gameManager.generateDealerCards
        gameManager = gameManager.clearDealerHand()
        gameManager = gameManager.addCardToDealerHand(FaceCard(Suits.Hearts, Ranks.Jack))
        gameManager = gameManager.addCardToDealerHand(FaceCard(Suits.Spades,Ranks.Ace))
        gameManager.containsCardTypeInDealerHand(Ranks.Ace) should be(1)
      }
      "containsCardType when not containing card" in {
        gameManager.generateDealerCards
        gameManager.clearDealerHand()
        gameManager.addCardToDealerHand(FaceCard(Suits.Hearts,Ranks.Ace))
        gameManager.addCardToDealerHand(FaceCard(Suits.Spades,Ranks.Ace))
        gameManager.containsCardTypeInDealerHand(Ranks.King) should be(-1)
      }
    }
  }
}
