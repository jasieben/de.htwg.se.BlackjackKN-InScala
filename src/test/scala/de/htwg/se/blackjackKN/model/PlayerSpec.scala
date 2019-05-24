package de.htwg.se.blackjackKN.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PlayerSpec extends WordSpec with Matchers {
  val player = Player("Your Name")
  "A Player" when {
    "new" should {
      "have a name" in {
        player.name should be("Your Name")
      }
      "have a nice String representation" in {
        player.toString should be("Your Name")
      }
      "have a balance of 0$" in {
        player.balance > 0 should be (true)
      }
    }
    "adding Card" should {
      "have added card in hand" in {
        player.addCardToHand(NumberCard(Suits.Diamonds, 9))
        player.getCard(0) should be (NumberCard(Suits.Diamonds,9))
      }
    }
    "clearing hand" should {
      "have no cards in hand" in {
        player.clearHand()
        player.getHandSize should be(0)
      }
    }
  }
  "adding a bet" should {
    "substract the bet amount" in {
      player.addBet(Bet(500))
      player.balance should be (500)
    }
  }
  "clearing the bets" should {
    "zero out all bets" in {
      player.addBet(Bet(500))
      player.clearBets()
      player.bet.value should be(0)
    }
  }
  "wanting to bet amount above balance" should {
    player.addBet(Bet(player.balance.toInt + 500))
    "not continue" in {
      player.bet.value should be (0)
    }
  }



}