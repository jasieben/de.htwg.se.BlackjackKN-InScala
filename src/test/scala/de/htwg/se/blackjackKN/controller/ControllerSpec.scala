package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.model.{Bet, FaceCard, NumberCard, Ranks, Suits}
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
      "notify its Observer after starting new round" in {
        controller.startNewRound()
        observer.updated should be(true)
        controller.player.getHandSize should be(2)
        controller.dealer.getHandSize should be(2)
      }
      "notify its Observer after standing" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.stand()
        controller.gameStates.contains(GameState.STAND) should be(true)
        controller.gameStates.contains(GameState.PLAYER_WINS) should be(true)
        observer.updated should be(true)
      }
      "notify its Observer after hitting" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player.addCardToHand(NumberCard(Suits.Clubs))

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
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.player.addCardToHand(FaceCard(Suits.Clubs, Ranks.Jack))
        controller.evaluate()
        controller.gameStates.contains(GameState.PLAYER_BUST) should be(true)

      }
      "display when the dealer busts" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.dealer.addCardToHand(FaceCard(Suits.Clubs, Ranks.Jack))
        controller.evaluate()

        controller.gameStates.contains(GameState.DEALER_BUST) should be(true)
      }
      "display when there is a push" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.evaluate()
        controller.gameStates.contains(GameState.PUSH) should be(true)
      }
      "display when the player looses" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player.addCardToHand(NumberCard(Suits.Hearts, 4))
        controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.dealer.getHandValue should be(19)
        controller.player.getHandValue should be(14)
        controller.revealDealer()
        controller.evaluate()
        controller.gameStates.contains(GameState.PLAYER_LOOSE) should be(true)
      }
      "display when the player has a Blackjack" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player.addCardToHand(FaceCard())
        controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.dealer.getHandValue should be(17)
        controller.player.getHandValue should be(21)
        controller.evaluate()
        controller.gameStates.contains(GameState.PLAYER_BLACKJACK) should be(true)
      }
      "display when the bet fails" in {
        controller.startNewRound()
        controller.player.balance = 0
        controller.setBet(100)
        controller.gameStates.contains(GameState.BET_FAILED) should be(true)
      }
      "control Ace Behavior when ace with low number card" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.player.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.player.addCardToHand(FaceCard(Suits.Diamonds, Ranks.Ace))
        controller.aceStrategy.execute()
        controller.gameStates.contains(GameState.ACE) should be(true)
      }
      "control Ace Behavior when double ace" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.player.addCardToHand(FaceCard(Suits.Diamonds, Ranks.Ace))
        controller.player.addCardToHand(FaceCard(Suits.Spades, Ranks.Ace))
        controller.checkForAces()
        controller.player.getHandValue should be(12)
      }
      "control Ace Behavior when evaluating" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.player.addCardToHand(NumberCard(Suits.Spades, 5))
        controller.player.addCardToHand(FaceCard(Suits.Diamonds, Ranks.Ace))
        controller.player.addCardToHand(FaceCard(Suits.Spades, Ranks.King))
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.evaluate()
        controller.player.getHandValue should be(16)
      }
      "renew the Card Deck if necessary" in {
        controller.startNewRound()
        while (controller.dealer.getCardDeckSize > 52) {
          controller.startNewRound()
        }
        controller.startNewRound()
        controller.dealer.getCardDeckSize should be >= 306
      }
    }
    "revealing cards" should {
      val controller = new Controller()
      controller.startGame()
      "draw (a) card(s) for the dealer" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 4))
        controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.gameStates.contains(GameState.DEALER_DRAWS) should be(true)
      }
      "not draw a card for the dealer" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.dealer.getHandValue should be(17)
      }
      "make correct decision when dealer and player have blackjack" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts))
        controller.dealer.addCardToHand(FaceCard(Suits.Clubs))
        controller.player.addCardToHand(NumberCard(Suits.Hearts))
        controller.player.addCardToHand(FaceCard(Suits.Clubs))
        controller.evaluate()
        controller.gameStates.contains(GameState.DEALER_BLACKJACK) should be(true)
        controller.gameStates.contains(GameState.PLAYER_BLACKJACK) should be(true)
      }
      "make correct decision when dealer has blackjack and player not" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts))
        controller.dealer.addCardToHand(FaceCard(Suits.Clubs))
        controller.player.addCardToHand(NumberCard(Suits.Hearts, 2))
        controller.player.addCardToHand(FaceCard(Suits.Clubs))
        controller.revealDealer()
        controller.evaluate()
        controller.gameStates.contains(GameState.DEALER_BLACKJACK) should be(true)
        controller.gameStates.contains(GameState.PLAYER_LOOSE) should be(true)
      }
    }
    "paying out bet" should {
      val controller = new Controller()
      controller.startGame()

      "pay correct amount when blackjack" in {
        controller.player.clearHand()
        controller.dealer.clearHand()
        controller.player.balance = 1000
        controller.setBet(100)
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player.addCardToHand(NumberCard(Suits.Hearts))
        controller.player.addCardToHand(FaceCard(Suits.Clubs, Ranks.Ace))
        controller.evaluate()
        controller.player.balance should be(1150)
        controller.player.bet = Bet(0)
      }
      "pay correct amount when push" in {
        controller.player.clearHand()
        controller.dealer.clearHand()
        controller.player.balance = 1000
        controller.setBet(100)
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 8))
        controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player.addCardToHand(NumberCard(Suits.Hearts, 8))
        controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.evaluate()
        controller.player.balance should be(1000)
        controller.player.bet = Bet(0)
      }
      "pay correct amount when loosing" in {
        controller.player.clearHand()
        controller.dealer.clearHand()
        controller.player.balance = 1000
        controller.setBet(100)
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.evaluate()
        controller.player.balance should be(900)
        controller.player.bet = Bet(0)
      }
      "pay correct amount when winning" in {
        controller.player.clearHand()
        controller.dealer.clearHand()
        controller.player.balance = 1000
        controller.setBet(100)
        controller.dealer.addCardToHand(NumberCard(Suits.Hearts, 7))
        controller.dealer.addCardToHand(NumberCard(Suits.Clubs))
        controller.player.addCardToHand(NumberCard(Suits.Hearts, 9))
        controller.player.addCardToHand(NumberCard(Suits.Clubs))
        controller.revealDealer()
        controller.evaluate()
        controller.player.balance should be(1100)
        controller.player.bet = Bet(0)
      }
    }
  }
}
