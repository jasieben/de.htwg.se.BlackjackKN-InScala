package de.htwg.se.blackjackKN.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.blackjackKN.model.personsComponent.{DealerInterface, PlayerInterface}
import de.htwg.se.blackjackKN.model.personsComponent.personsBaseImpl.{Dealer, Player}
import de.htwg.se.blackjackKN.util.Command

class HitCommand(controller: Controller) extends Command {
  var dealerMemento : DealerInterface = controller.dealer
  var playerMemento : PlayerInterface = controller.player
  var revealedMemento : Boolean = controller.revealed
  override def doStep(): Unit = {
    dealerMemento = controller.dealer.copy()
    playerMemento = controller.player.copy()
    revealedMemento = controller.revealed
    controller.hit()
  }

  override def redoStep(): Unit = {
    val newDealerMemento = controller.dealer.copy()
    val newPlayerMemento = controller.player.copy()
    val newRevealedMemento : Boolean = controller.revealed

    controller.dealer = dealerMemento
    controller.player = playerMemento
    controller.revealed = revealedMemento

    dealerMemento = newDealerMemento
    playerMemento = newPlayerMemento
    revealedMemento = newRevealedMemento
    controller.hit()
  }

  override def undoStep(): Unit = {
    val newDealerMemento = controller.dealer.copy()
    val newPlayerMemento = controller.player.copy()
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
