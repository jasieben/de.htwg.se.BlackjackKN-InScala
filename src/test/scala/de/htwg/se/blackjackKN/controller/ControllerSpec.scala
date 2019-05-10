package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.model.{FaceCard, NumberCard}
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
      "notify its Observer after game initialized" in {
        controller.startGame()
        observer.updated should be(true)
        controller.dealer.generateDealerCards.nonEmpty
      }
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
        controller.dealer.addCardToHand(NumberCard("hearts", 7))
        controller.dealer.addCardToHand(NumberCard("clubs"))
        controller.player.addCardToHand(NumberCard("hearts", 9))
        controller.player.addCardToHand(NumberCard("clubs"))
        controller.stand()
        controller.output.contains("You win!") should be(true)
        observer.updated should be(true)
      }
      "notify its Observer after hitting" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard("hearts", 7))
        controller.dealer.addCardToHand(NumberCard("clubs"))
        controller.player.addCardToHand(NumberCard("hearts", 9))
        controller.player.addCardToHand(NumberCard("clubs"))

        controller.hit()
        controller.player.getHandSize should be(3)
        observer.updated should be(true)
      }
      "output the correct value" in {
        controller.display should be(controller.output)
      }
      "not be able to hit when already bust" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard("hearts", 7))
        controller.dealer.addCardToHand(NumberCard("clubs"))
        controller.player.addCardToHand(NumberCard("hearts", 9))
        controller.player.addCardToHand(NumberCard("clubs"))
        controller.player.addCardToHand(NumberCard("spades", 9))
        controller.hit()
        controller.output.contains("You cannot hit!") should be(true)
      }
    }

    "evaluating the result" should {
      val controller = new Controller()
      controller.startGame()
      "display when the player busts" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard("hearts", 7))
        controller.dealer.addCardToHand(NumberCard("clubs"))
        controller.player.addCardToHand(NumberCard("hearts", 9))
        controller.player.addCardToHand(NumberCard("clubs"))
        controller.player.addCardToHand(FaceCard("clubs", "jack"))
        controller.evaluate()
        controller.output.contains("You bust!") should be(true)

      }
      "display when the dealer busts" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard("hearts", 7))
        controller.dealer.addCardToHand(NumberCard("clubs"))
        controller.player.addCardToHand(NumberCard("hearts", 9))
        controller.player.addCardToHand(NumberCard("clubs"))
        controller.dealer.addCardToHand(FaceCard("clubs", "jack"))
        controller.evaluate()

        controller.output.contains("The dealer busts") should be(true)
      }
      "display when there is a push" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard("hearts", 9))
        controller.dealer.addCardToHand(NumberCard("clubs"))
        controller.player.addCardToHand(NumberCard("hearts", 9))
        controller.player.addCardToHand(NumberCard("clubs"))
        controller.dealer.getHandValue should be(19)
        controller.player.getHandValue should be(19)
        controller.revealDealer()
        controller.evaluate()
        controller.output.contains("Push") should be(true)
      }
      "display when the player looses" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard("hearts", 9))
        controller.dealer.addCardToHand(NumberCard("clubs"))
        controller.player.addCardToHand(NumberCard("hearts", 4))
        controller.player.addCardToHand(NumberCard("clubs"))
        controller.dealer.getHandValue should be(19)
        controller.player.getHandValue should be(14)
        controller.revealDealer()
        controller.evaluate()
        controller.output.contains("loose") should be(true)
      }
      "display when the player has a Blackjack" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard("hearts", 7))
        controller.dealer.addCardToHand(NumberCard("clubs"))
        controller.player.addCardToHand(FaceCard())
        controller.player.addCardToHand(NumberCard("clubs"))
        controller.dealer.getHandValue should be(17)
        controller.player.getHandValue should be(21)
        controller.evaluate()
        controller.output.contains("blackjack") should be(true)
      }
    }
    "revealing cards" should {
      val controller = new Controller()
      controller.startGame()
      "draw (a) card(s) for the dealer" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard("hearts", 4))
        controller.dealer.addCardToHand(NumberCard("clubs"))
        controller.player.addCardToHand(NumberCard("hearts", 9))
        controller.player.addCardToHand(NumberCard("clubs"))
        controller.revealDealer()
        controller.output.contains("The dealer draws a") should be(true)
      }
      "not draw a card for the dealer" in {
        controller.startNewRound()
        controller.dealer.clearHand()
        controller.player.clearHand()
        controller.dealer.addCardToHand(NumberCard("hearts", 7))
        controller.dealer.addCardToHand(NumberCard("clubs"))
        controller.player.addCardToHand(NumberCard("hearts", 9))
        controller.player.addCardToHand(NumberCard("clubs"))
        controller.revealDealer()
        controller.dealer.getHandValue should be(17)
      }
    }
  }
}
