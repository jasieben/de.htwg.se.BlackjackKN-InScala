package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.util.Command
import de.htwg.se.blackjackKN.controller.Controller
import de.htwg.se.blackjackKN.model.{Dealer, Player}

class SetCommand(player: Player, dealer: Dealer, controller: Unit) extends Command {
  override def doStep: Unit = {

  }

  override def redoStep: Unit = {}

  override def undoStep: Unit = controller = controller.set
}
