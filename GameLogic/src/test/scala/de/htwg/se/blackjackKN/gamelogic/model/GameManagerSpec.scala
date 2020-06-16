package de.htwg.se.blackjackKN.gamelogic.model

import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.CardInterface
import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.cardsBaseImpl.{FaceCard, NumberCard}
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GameManagerSpec extends WordSpec with Matchers {
  "The GameManager" when {
    var gameManager = GameManager(None)
    "generating Cards" should {
      "give the right amount of cards" in {
        gameManager = gameManager.generateDealerCards
        gameManager.cardDeck.size should be(260)
      }

      "renew the card deck correctly" in {
        gameManager.generateDealerCards.cardDeck.size should be(5 * 52)
      }
    }
    "drawing Card" should {
      "be a card" in {
        val ogSize = gameManager.cardDeck.size
        gameManager.drawCard() should (be(a[NumberCard]) or be(a[FaceCard]))
        gameManager = gameManager.dropCard()
        gameManager.cardDeck.size should be(ogSize - 1)
      }
      "add to Player hand" in {
        var testGameManager = gameManager.addPlayerToGame("123")
        testGameManager = testGameManager.addCardToPlayerHand(0, gameManager.drawCard()).dropCard()

        testGameManager.playerHands.head.size should be(1)
      }
    }
    "clearing a player hand" should {
      "clear the player hand" in {
        var testCase2GameManager = gameManager.addPlayerToGame("123")
        testCase2GameManager = testCase2GameManager.clearPlayerHand(0)

        testCase2GameManager.playerHands.head.size should be(0)
      }
    }
    "managing the player" should {
      "be able to get a player card" in {
        var testCase3GameManager = gameManager.addPlayerToGame("123")
        testCase3GameManager = testCase3GameManager.addCardToPlayerHand(0, gameManager.drawCard()).dropCard()
        testCase3GameManager.getPlayerCard(0,0) should be(a[CardInterface])
      }
      "get the player hand size" in {
        var testCase4GameManager = gameManager.addPlayerToGame("123")
        testCase4GameManager = testCase4GameManager.addCardToPlayerHand(0, gameManager.drawCard()).dropCard()
        testCase4GameManager = testCase4GameManager.addCardToPlayerHand(0, gameManager.drawCard()).dropCard()
        testCase4GameManager.getPlayerHandSize(0) should be(2)
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
