package de.htwg.se.blackjackKN.model

import de.htwg.se.blackjackKN.model
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
        player.balance should be(0)
      }
    }
    "adding Card" should {
      "have added card in hand" in {
        player.addCardToHand(NumberCard("diamonds", "9"))
        player.getCard(0) should be (NumberCard("diamonds","9"))
      }
    }
    "clearing hand" should {
      "have no cards in hand" in {
        player.clearHand()
        player.getHandSize should be(0)
      }
    }
  }



}