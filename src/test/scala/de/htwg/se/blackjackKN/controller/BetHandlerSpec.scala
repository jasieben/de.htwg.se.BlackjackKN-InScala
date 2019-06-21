package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.controller.ControllerBaseImpl._
import de.htwg.se.blackjackKN.controller.GameState
import de.htwg.se.blackjackKN.model.{Bet, Player}
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
