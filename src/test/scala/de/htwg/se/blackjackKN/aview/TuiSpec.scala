package de.htwg.se.blackjackKN.aview

import de.htwg.se.blackjackKN.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.blackjackKN.controller.controllerComponent.GameState
import de.htwg.se.blackjackKN.controller.controllerComponent.GameState.GameState
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}
import de.htwg.se.blackjackKN.model.cardsComponent.CardInterface

@RunWith(classOf[JUnitRunner])
class TuiSpec extends WordSpec with Matchers{
  "The Tui" should {
    val controller : Controller = new Controller()
    val tui = new Tui(controller)
    controller.startGame()
    "process n" in {
      tui.processInput("n")
      controller.dealer.drawCard() should be(a [CardInterface])
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
    "process z" in {
      controller.startNewRound()
      tui.processInput("z")
      tui.output.contains("Undo") should be(true)
    }
    "process y" in {
      controller.startNewRound()
      tui.processInput("y")
      tui.output.contains("Redo") should be(true)
    }
    "process b" in {
      controller.startNewRound()
      tui.processInput("b")
      tui.output.contains("balance") should be(true)
    }
    "process create test" in {
      controller.startNewRound()
      tui.processInput("create test")
      tui.output.contains("name") should be(true)
    }
    "process n 100" in {
      controller.startNewRound()
      tui.processInput("n 100")
      tui.output.contains("The dealer") should be(true)
    }
    "process exit" in {
      tui.processInput("exit")
      tui.output.contains("Exiting") should be(true)
    }

    "display when shuffling" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.SHUFFLING
      controller.notifyObservers()
      tui.output.contains("shuffled") should be(true)
    }
    "display when Dealer draws cards" in {
      controller.startNewRound()
      controller.dealer = controller.dealer.addCardToHand(controller.dealer.drawCard())
      controller.dealer = controller.dealer.addCardToHand(controller.dealer.drawCard())
      controller.gameStates = controller.gameStates :+ GameState.DEALER_DRAWS
      controller.notifyObservers()
      tui.output.contains("dealer draws") should be(true)
    }
    "display when Dealer busts" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.DEALER_BUST
      controller.notifyObservers()
      tui.output.contains("dealer busts") should be(true)
    }
    "display when blackjack" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.PLAYER_BLACKJACK
      controller.notifyObservers()
      tui.output.contains("blackjack") should be(true)
    }
    "display when Player looses" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.PLAYER_LOOSE
      controller.notifyObservers()
      tui.output.contains("loose") should be(true)
    }
    "display when push" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.PUSH
      controller.notifyObservers()
      tui.output.contains("Push") should be(true)
    }
    "display when player busts" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.PLAYER_BUST
      controller.notifyObservers()
      tui.output.contains("bust") should be(true)
    }
    "display when player wins" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.PLAYER_WINS
      controller.notifyObservers()
      tui.output.contains("win") should be(true)
    }
    "display when in Idle status" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.IDLE
      controller.notifyObservers()
      tui.output.contains("new") should be(true)
    }
    "display when Ace happens" in {
      controller.clearGameStates()
      tui.gamestatePointer = 0
      tui.firstAceMessage = false
      controller.gameStates = controller.gameStates :+ GameState.ACE
      controller.notifyObservers()
      tui.output.contains("or") should be(true)
    }
    "display when Bet fails" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.BET_FAILED
      controller.notifyObservers()
      tui.output.contains("failed") should be(true)
    }
    "display when setting Bet" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.BET_SET
      controller.notifyObservers()
      tui.output.contains("set") should be(true)
    }
    "display when undoing" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.UNDO
      controller.notifyObservers()
      tui.output.contains("Undo") should be(true)
    }
    "display when redoing" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.REDO
      controller.notifyObservers()
      tui.output.contains("Redo") should be(true)
    }
    "display when setting name" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.NEW_NAME
      controller.notifyObservers()
      tui.output.contains("name") should be(true)
    }
    "display when DealerBlackjack" in {
      controller.startNewRound()
      controller.gameStates = controller.gameStates :+ GameState.DEALER_BLACKJACK
      controller.notifyObservers()
      tui.output.contains("Blackjack") should be(true)
    }
  }
}
