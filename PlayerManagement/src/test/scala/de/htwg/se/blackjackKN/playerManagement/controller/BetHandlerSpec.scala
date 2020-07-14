package de.htwg.se.blackjackKN.playerManagement.controller

import de.htwg.se.blackjackKN.playerManagement.model.{Bet, EndState, Player}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class BetHandlerSpec extends WordSpec with Matchers{
"A BetHandler " when {
    val win = new WinningHandler(None)
    val loose = new LoosingHandler(Option(win))
    val blackjack = new BlackjackHandler(Option(loose))
    val push = new PushHandler(Option(blackjack))
  "handle Winning Request" in {
    val player = Player(bet = Option(Bet(None, 100)))

    push.handleRequest(EndState.PLAYER_WINS, player).balance should be (1200)
  }
}
}
