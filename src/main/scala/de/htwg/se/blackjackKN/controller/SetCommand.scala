package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.util.Command
import de.htwg.se.blackjackKN.controller.Controller
import de.htwg.se.blackjackKN.model.{Dealer, Player}

class SetCommand(player: Player, dealer: Dealer, controller: Controller) extends Command {
  override def doStep: Unit = {
    controller.dealer = dealer
    controller.player = player
  }

  override def redoStep: Unit = {
    controller.dealer = dealer
    controller.player = player
  }

  override def undoStep: Unit = {
    controller.dealer = dealer
    controller.player = player
  }
}
