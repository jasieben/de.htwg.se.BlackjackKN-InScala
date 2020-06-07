package de.htwg.se.blackjackKN.gamelogic.controller


import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.GameState
import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.blackjackKN.gamelogic.model.{Ranks, Suits}
import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.cardsBaseImpl.{FaceCard, NumberCard}
import de.htwg.se.blackjackKN.gamelogic.util.Observer
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ControllerSpec extends WordSpec with Matchers {
  "A Controller" when {
    "observed by an Observer" should {
      val controller = new Controller()
      val observer = new Observer {
        var updated: Boolean = false

        def isUpdated: Boolean = updated

        override def update: Boolean = {
          updated = true;
          updated
        }
      }
      controller.add(observer)
      controller.startGame()
      "notify its Observer after starting new round" in {
        controller.startNewRound()
        observer.updated should be(true)
        controller.gameManager.getPlayerHandSize(0) should be(2)
      }
      "notify its Observer after standing" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts, 7))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Clubs))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Hearts, 9))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Clubs))
        controller.stand()
        controller.gameStates.contains(GameState.STAND) should be(true)
        controller.gameStates.contains(GameState.PLAYER_WINS) should be(true)
        observer.updated should be(true)
      }
      "notify its Observer after hitting" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts, 7))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Clubs))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Hearts, 9))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Clubs))

        controller.hit()
        controller.gameManager.getPlayerHandSize(0) should be(3)
        observer.updated should be(true)
      }
    }

    "evaluating the result" should {
      val controller = new Controller()
      controller.startGame()
      "display when the player busts" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts, 7))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Clubs))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Hearts, 9))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Clubs))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, FaceCard(Suits.Clubs, Ranks.Jack))
        controller.evaluate()
        controller.gameStates.contains(GameState.PLAYER_BUST) should be(true)

      }
      "display when the dealer busts" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts, 7))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Clubs))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Hearts, 9))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Clubs))
        controller.gameManager = controller.gameManager.addCardToDealerHand(FaceCard(Suits.Clubs, Ranks.Jack))
        controller.evaluate()

        controller.gameStates.contains(GameState.DEALER_BUST) should be(true)
      }
      "display when there is a push" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts, 9))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Clubs))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Hearts, 9))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.evaluate()
        controller.gameStates.contains(GameState.PUSH) should be(true)
      }
      "display when the player looses" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts, 9))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Clubs))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Hearts, 4))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Clubs))
        controller.gameManager.getDealerHandValue should be(19)
        controller.gameManager.getPlayerHandValue(0) should be(14)
        controller.revealDealer()
        controller.evaluate()
        controller.gameStates.contains(GameState.PLAYER_LOOSE) should be(true)
      }
      "display when the player wins" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts, 4))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Clubs))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0,NumberCard(Suits.Hearts, 9))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Clubs))
        controller.gameManager.getDealerHandValue should be(14)
        controller.gameManager.getPlayerHandValue(0) should be(19)
        controller.revealDealer()
        controller.evaluate()
        controller.gameStates.contains(GameState.PLAYER_WINS) should be(true)
      }
      "display when the player has a Blackjack" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts, 7))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Clubs))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, FaceCard())
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Clubs))
        controller.gameManager.getDealerHandValue should be(17)
        controller.gameManager.getPlayerHandValue(0) should be(21)
        controller.evaluate()
        controller.gameStates.contains(GameState.PLAYER_BLACKJACK) should be(true)
      }
      "control Ace Behavior when ace with low number card" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Hearts, 7))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, FaceCard(Suits.Diamonds, Ranks.Ace))
        controller.aceStrategy.execute()
        controller.gameStates.contains(GameState.ACE) should be(true)
      }
      "control Ace Behavior when double ace" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, FaceCard(Suits.Diamonds, Ranks.Ace))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, FaceCard(Suits.Spades, Ranks.Ace))
        controller.checkForAces()
        controller.gameManager.getPlayerHandValue(0) should be(12)
      }
      "control Ace Behavior when evaluating" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Spades, 5))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, FaceCard(Suits.Diamonds, Ranks.Ace))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, FaceCard(Suits.Spades, Ranks.King))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts, 7))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Clubs))
        controller.evaluate()
        controller.gameManager.getPlayerHandValue(0) should be(16)
      }
      "renew the Card Deck if necessary" in {
        controller.startNewRound()
        while (controller.gameManager.cardDeck.size > 52) {
          controller.startNewRound()
        }
        controller.startNewRound()
        controller.gameManager.cardDeck.size should be >= 250
      }
    }
    "revealing cards" should {
      val controller = new Controller()
      controller.startGame()
      "draw (a) card(s) for the dealer" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts, 4))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Clubs))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Hearts, 9))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.gameStates.contains(GameState.DEALER_DRAWS) should be(true)
      }
      "not draw a card for the dealer" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts, 7))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Clubs))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Hearts, 9))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.gameManager.getDealerHandValue should be(17)
      }
      "make correct decision when dealer and player have blackjack" in {
        controller.startNewRound()
        controller.clearGameStates()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts))
        controller.gameManager = controller.gameManager.addCardToDealerHand(FaceCard(Suits.Clubs))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Hearts))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, FaceCard(Suits.Clubs))
        controller.evaluate()
        controller.gameStates.contains(GameState.PUSH) should be(true)
      }
      "make correct decision when dealer has blackjack and player not" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts))
        controller.gameManager = controller.gameManager.addCardToDealerHand(FaceCard(Suits.Clubs))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Hearts, 2))
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, FaceCard(Suits.Clubs))
        controller.revealDealer()
        controller.evaluate()
        controller.gameStates.contains(GameState.DEALER_BLACKJACK) should be(true)
        controller.gameStates.contains(GameState.PLAYER_LOOSE) should be(true)
      }
    }
    "making gamestate altering commands" should {
      val controller = new Controller()
      controller.startGame()
      "as undo after hitting" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Clubs, 3))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts))
        controller.gameManager = controller.gameManager.addCardToDealerHand(FaceCard(Suits.Clubs))
        controller.hitCommand()
        val testValue = controller.gameManager.getPlayerHandValue(0)
        controller.hitCommand()
        controller.undo()
        controller.gameManager.getPlayerHandValue(0) should be(testValue)
      }
      "undo hitting" in {
        controller.hitCommand()
        val testValue = controller.gameManager.getPlayerHandValue(0)
        controller.standCommand()
        controller.undo()
        controller.gameManager.getPlayerHandValue(0) should be(testValue)
      }
      "redo after hitting" in {
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Hearts))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts))
        controller.gameManager = controller.gameManager.addCardToDealerHand(FaceCard(Suits.Clubs))

        controller.hitCommand()
        val testValue = controller.gameManager.getPlayerHandValue(0)
        controller.undo()
        controller.redo()
        controller.gameManager.getPlayerHandValue(0) should be(testValue)
      }
      "as redo after undo" in {
        controller.startNewRound()
        controller.gameManager = controller.gameManager.clearDealerHand()
        controller.gameManager = controller.gameManager.clearPlayerHand(0)
        controller.gameManager = controller.gameManager.addCardToPlayerHand(0, NumberCard(Suits.Hearts))
        controller.gameManager = controller.gameManager.addCardToDealerHand(NumberCard(Suits.Hearts))
        controller.gameManager = controller.gameManager.addCardToDealerHand(FaceCard(Suits.Clubs))
        controller.hitCommand()
        controller.standCommand()
        val testValue = controller.gameManager.getPlayerHandValue(0)
        controller.undo()
        controller.redo()
        controller.gameManager.getPlayerHandValue(0) should be(testValue)
      }
    }
  }
}
