package de.htwg.se.blackjackKN.aview

import de.htwg.se.blackjackKN.controller.{Controller, GameState}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import de.htwg.se.blackjackKN.model.Card

@RunWith(classOf[JUnitRunner])
class TuiSpec extends WordSpec with Matchers{
  "The Tui" should {
    val controller : Controller = new Controller()
    val tui = new Tui(controller)
    controller.startGame()
    "process n" in {
      tui.processInput("n")
      controller.dealer.drawCard() should be(a [Card])
    }
    "process h" in {
      controller.startNewRound()
      tui.processInput("h")
      tui.output.contains("Test hits") should be(true)
    }
    "process s" in {
      controller.startNewRound()
      tui.processInput("s")
      tui.output.contains("Test stands") should be(true)
    }
    "process Scala ist toll!" in {
      tui.processInput("Scala ist toll!")
    }
    "display when shuffling" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.SHUFFLING
      controller.notifyObservers
      tui.output.contains("shuffled") should be(true)
    }
    "display when Dealer draws cards" in {
      controller.startNewRound()
      controller.dealer.addCardToHand(controller.dealer.drawCard())
      controller.dealer.addCardToHand(controller.dealer.drawCard())
      controller.gameStates = controller.gameStates :+ GameState.DEALER_DRAWS
      controller.notifyObservers
      tui.output.contains("dealer draws") should be(true)
    }
    "display when Dealer busts" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.DEALER_BUST
      controller.notifyObservers
      tui.output.contains("dealer busts") should be(true)
    }
    "display when blackjack" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.PLAYER_BLACKJACK
      controller.notifyObservers
      tui.output.contains("blackjack") should be(true)
    }
    "display when Player looses" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.PLAYER_LOOSE
      controller.notifyObservers
      tui.output.contains("loose") should be(true)
    }
    "display when push" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.PUSH
      controller.notifyObservers
      tui.output.contains("Push") should be(true)
    }
    "display when player busts" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.PLAYER_BUST
      controller.notifyObservers
      tui.output.contains("bust") should be(true)
    }
    "display when player wins" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.PLAYER_WINS
      controller.notifyObservers
      tui.output.contains("win") should be(true)
    }
  }
}
