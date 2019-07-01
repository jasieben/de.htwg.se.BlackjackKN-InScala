package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.controller.controllerComponent.controllerBaseImpl.{BlackjackHandler, LoosingHandler, PushHandler, WinningHandler}
import de.htwg.se.blackjackKN.controller.controllerComponent.GameState
import de.htwg.se.blackjackKN.model.betComponent.Bet
import de.htwg.se.blackjackKN.model.personsComponent.personsBaseImpl.Player
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class BetHandlerSpec extends WordSpec with Matchers{
"A BetHandler " when {
    val win = new WinningHandler(null)
    val loose = new LoosingHandler(win)
    val blackjack = new BlackjackHandler(loose)
    val push = new PushHandler(blackjack)
  "handle Winning Request" in {
    val player = Player()
    player.bet = Bet(100)
    push.handleRequest(GameState.PLAYER_WINS, player)
    player.balance should be (1200)
  }
}
}
