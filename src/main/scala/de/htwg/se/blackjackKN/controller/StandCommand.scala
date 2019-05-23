package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.model.{Dealer, Player}
import de.htwg.se.blackjackKN.util.Command

class StandCommand(controller: Controller) extends Command {
  var dealerMemento : Dealer = controller.dealer
  var playerMemento : Player = controller.player
  override def doStep: Unit = {
    dealerMemento = controller.dealer.copy()
    playerMemento = controller.player.copy()
    controller.stand()
  }

  override def redoStep: Unit = {
    val newDealerMemento : Dealer = controller.dealer.copy()
    val newPlayerMemento : Player = controller.player.copy()

    controller.dealer = dealerMemento
    controller.player = playerMemento

    dealerMemento = newDealerMemento
    playerMemento = newPlayerMemento
  }

  override def undoStep: Unit = {
    val newDealerMemento : Dealer = controller.dealer.copy()
    val newPlayerMemento : Player = controller.player.copy()

    controller.dealer = dealerMemento
    controller.player = playerMemento

    dealerMemento = newDealerMemento
    playerMemento = newPlayerMemento
  }
}
