package de.htwg.se.blackjackKN.playerManagement.model

import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
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