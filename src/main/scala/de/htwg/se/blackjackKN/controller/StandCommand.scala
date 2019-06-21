package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.model.Player
import de.htwg.se.blackjackKN.model.personsComponent.personsBaseImpl.{Dealer, Player}
import de.htwg.se.blackjackKN.util.Command

class StandCommand(controller: Controller) extends Command {
  var dealerMemento : Dealer = controller.dealer
  var playerMemento : Player = controller.player
  var revealedMemento : Boolean = controller.revealed
  override def doStep: Unit = {
    dealerMemento = controller.dealer.copy()
    playerMemento = controller.player.copy()
    revealedMemento = controller.revealed
    controller.stand()
  }

  override def redoStep: Unit = {
    val newDealerMemento : Dealer = controller.dealer.copy()
    val newPlayerMemento : Player = controller.player.copy()
    val newRevealedMemento : Boolean = controller.revealed

    controller.dealer = dealerMemento
    controller.player = playerMemento
    controller.revealed = revealedMemento

    dealerMemento = newDealerMemento
    playerMemento = newPlayerMemento
    revealedMemento = newRevealedMemento
    controller.stand()
  }

  override def undoStep: Unit = {
    val newDealerMemento : Dealer = controller.dealer.copy()
    val newPlayerMemento : Player = controller.player.copy()
    val newRevealedMemento : Boolean = controller.revealed

    controller.dealer = dealerMemento
    controller.player = playerMemento
    controller.revealed = revealedMemento

    dealerMemento = newDealerMemento
    playerMemento = newPlayerMemento
    revealedMemento = newRevealedMemento
    controller.evaluate()
  }
}
