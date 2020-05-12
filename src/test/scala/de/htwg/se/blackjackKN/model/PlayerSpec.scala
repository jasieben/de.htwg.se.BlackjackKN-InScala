package de.htwg.se.blackjackKN.model

import de.htwg.se.blackjackKN.model.betComponent.Bet
import de.htwg.se.blackjackKN.model.cardsComponent.cardsBaseImpl.NumberCard
import de.htwg.se.blackjackKN.model.personsComponent.Player
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PlayerSpec extends WordSpec with Matchers {
  var player = Player()
  "A Player" when {
    "new" should {
      "have a name" in {
        player.name should be("Test")
      }
      "have a nice String representation" in {
        player.toString should be("Test")
      }
      "have a balance of over 0$" in {
        player.balance > 0 should be(true)
      }
    }
    "adding Card" should {
      "have added card in hand" in {
        player = player.addCardToHand(NumberCard(Suits.Diamonds, 9))
        player.getCard(0) should be(NumberCard(Suits.Diamonds, 9))
      }
    }
    "clearing hand" should {
      "have no cards in hand" in {
        player = player.clearHand()
        player.getHandSize should be(0)
      }
    }
  }
  "adding a bet" should {
    "substract the bet amount" in {
      player = player.newBet(500)
      player.balance should be(500)
    }
  }
  "clearing the bets" should {
    "zero out all bets" in {
      player = player.newBet(500)
      player = player.copy(bet = None)
      player.bet should be(None)
    }
  }
  "wanting to bet amount above balance" should {
    player = player.newBet(player.balance + 500)
    "not continue" in {
      player.bet should be(None)
    }
  }


}