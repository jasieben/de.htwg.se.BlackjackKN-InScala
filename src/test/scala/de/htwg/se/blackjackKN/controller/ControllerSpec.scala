package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.blackjackKN.controller.controllerComponent.GameState
import de.htwg.se.blackjackKN.model.betComponent.Bet
import de.htwg.se.blackjackKN.model.cardsComponent.cardsBaseImpl.{FaceCard, NumberCard}
import de.htwg.se.blackjackKN.model.{Ranks, Suits}
import de.htwg.se.blackjackKN.util.Observer
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

        override def update: Boolean = {updated = true; updated}
      }
      controller.add(observer)
      controller.startGame()
      "notify its Observer after starting new round" in {
        controller.startNewRound()
        observer.updated should be(true)
        controller.player.getHandSize should be(2)
      }
      "notify its Observer after standing" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.stand()
        controller.gameStates.contains(GameState.STAND) should be(true)
        controller.gameStates.contains(GameState.PLAYER_WINS) should be(true)
        observer.updated should be(true)
      }
      "notify its Observer after hitting" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Clubs))

        controller.hit()
        controller.player.getHandSize should be(3)
        observer.updated should be(true)
      }
    }

    "evaluating the result" should {
      val controller = new Controller()
      controller.startGame()
      "display when the player busts" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(FaceCard(Suits.Clubs, Ranks.Jack))
        controller.evaluate()
        controller.gameStates.contains(GameState.PLAYER_BUST) should be(true)

      }
      "display when the dealer busts" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.dealer = controller.dealer.addCardToHand(FaceCard(Suits.Clubs, Ranks.Jack))
        controller.evaluate()

        controller.gameStates.contains(GameState.DEALER_BUST) should be(true)
      }
      "display when there is a push" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.evaluate()
        controller.gameStates.contains(GameState.PUSH) should be(true)
      }
      "display when the player looses" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts, 4))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.dealer.getHandValue should be(19)
        controller.player.getHandValue should be(14)
        controller.revealDealer()
        controller.evaluate()
        controller.gameStates.contains(GameState.PLAYER_LOOSE) should be(true)
      }
      "display when the player wins" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 4))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.dealer.getHandValue should be(14)
        controller.player.getHandValue should be(19)
        controller.revealDealer()
        controller.evaluate()
        controller.gameStates.contains(GameState.PLAYER_WINS) should be(true)
      }
      "display when the player has a Blackjack" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(FaceCard())
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.dealer.getHandValue should be(17)
        controller.player.getHandValue should be(21)
        controller.evaluate()
        controller.gameStates.contains(GameState.PLAYER_BLACKJACK) should be(true)
      }
      "display when the bet fails" in {
        controller.startNewRound()
        controller.player = controller.player.copy(balance = 0)
        controller.setBet(100)
        controller.gameStates.contains(GameState.BET_FAILED) should be(true)
      }
      "change player when loading new one in" in {
        controller.createNewPlayer("TestUser1")
        controller.createNewPlayer("TestUser2")
        controller.changePlayer("TestUser1")
        controller.player.name should be("TestUser1")

      }
      "control Ace Behavior when ace with low number card" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.player = controller.player.addCardToHand(FaceCard(Suits.Diamonds, Ranks.Ace))
        controller.aceStrategy.execute()
        controller.gameStates.contains(GameState.ACE) should be(true)
      }
      "control Ace Behavior when double ace" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.player = controller.player.addCardToHand(FaceCard(Suits.Diamonds, Ranks.Ace))
        controller.player = controller.player.addCardToHand(FaceCard(Suits.Spades, Ranks.Ace))
        controller.checkForAces()
        controller.player.getHandValue should be(12)
      }
      "control Ace Behavior when evaluating" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Spades, 5))
        controller.player = controller.player.addCardToHand(FaceCard(Suits.Diamonds, Ranks.Ace))
        controller.player = controller.player.addCardToHand(FaceCard(Suits.Spades, Ranks.King))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.evaluate()
        controller.player.getHandValue should be(16)
      }
      "renew the Card Deck if necessary" in {
        controller.startNewRound()
        while (controller.dealer.cardDeck.size > 52) {
          controller.startNewRound()
        }
        controller.startNewRound()
        controller.dealer.cardDeck.size should be >= 250
      }
    }
    "revealing cards" should {
      val controller = new Controller()
      controller.startGame()
      "draw (a) card(s) for the dealer" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 4))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.gameStates.contains(GameState.DEALER_DRAWS) should be(true)
      }
      "not draw a card for the dealer" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.dealer.getHandValue should be(17)
      }
      "make correct decision when dealer and player have blackjack" in {
        controller.startNewRound()
        controller.clearGameStates()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts))
        controller.dealer = controller.dealer.addCardToHand(FaceCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts))
        controller.player = controller.player.addCardToHand(FaceCard(Suits.Clubs))
        controller.evaluate()
        controller.gameStates.contains(GameState.PUSH) should be(true)
      }
      "make correct decision when dealer has blackjack and player not" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts))
        controller.dealer = controller.dealer.addCardToHand(FaceCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts, 2))
        controller.player = controller.player.addCardToHand(FaceCard(Suits.Clubs))
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
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Clubs, 3))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts))
        controller.dealer = controller.dealer.addCardToHand(FaceCard(Suits.Clubs))
        controller.hitCommand()
        val testValue = controller.player.getHandValue
        controller.hitCommand()
        controller.undo()
        controller.player.getHandValue should be(testValue)
      }
      "undo hitting" in {
        controller.hitCommand()
        val testValue = controller.player.getHandValue
        controller.standCommand()
        controller.undo()
        controller.player.getHandValue should be(testValue)
      }
      "redo after hitting" in {
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts))
        controller.dealer = controller.dealer.addCardToHand(FaceCard(Suits.Clubs))

        controller.hitCommand()
        val testValue = controller.player.getHandValue
        controller.undo()
        controller.redo()
        controller.player.getHandValue should be(testValue)
      }
      "as redo after undo" in {
        controller.startNewRound()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.clearHand()
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts))
        controller.dealer = controller.dealer.addCardToHand(FaceCard(Suits.Clubs))
        controller.hitCommand()
        controller.standCommand()
        val testValue = controller.player.getHandValue
        controller.undo()
        controller.redo()
        controller.player.getHandValue should be(testValue)
      }
    }
    "paying out bet" should {
      val controller = new Controller()
      controller.startGame()

      "pay correct amount when blackjack" in {
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.copy(balance = 1000)
        controller.setBet(100)
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts))
        controller.player = controller.player.addCardToHand(FaceCard(Suits.Clubs, Ranks.Ace))
        controller.evaluate()
        controller.player.balance should be(1150)
        controller.player = controller.player.copy(bet = None)
      }
      "pay correct amount when push" in {
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.copy(balance = 1000)
        controller.setBet(100)
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 8))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts, 8))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.evaluate()
        controller.player.balance should be(1000)
        controller.player = controller.player.copy(bet = None)
      }
      "pay correct amount when loosing" in {
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.copy(balance = 1000)
        controller.setBet(100)
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.evaluate()
        controller.player.balance should be(900)
        controller.player = controller.player.copy(bet = None)
      }
      "pay correct amount when winning" in {
        controller.player = controller.player.clearHand()
        controller.dealer = controller.dealer.clearHand()
        controller.player = controller.player.copy(balance = 1000)
        controller.setBet(100)
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer = controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player = controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.evaluate()
        controller.player.balance should be(1100)
        controller.player = controller.player.copy(bet = None)
      }
    }
  }
}
