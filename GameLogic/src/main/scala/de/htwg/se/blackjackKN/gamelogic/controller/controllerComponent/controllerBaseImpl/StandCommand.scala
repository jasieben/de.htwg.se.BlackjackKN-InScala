package de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.blackjackKN.gamelogic.model.GameManager
import de.htwg.se.blackjackKN.gamelogic.util.Command

class StandCommand(controller: Controller) extends Command {
  var gameManagerMemento : GameManager = controller.gameManager
  var revealedMemento : Boolean = controller.revealed
  override def doStep(): Unit = {
    gameManagerMemento = controller.gameManager.copy()
    revealedMemento = controller.revealed
    controller.stand()
  }

  override def redoStep(): Unit = {
    val newGameManagerMemento = controller.gameManager.copy()
    val newRevealedMemento : Boolean = controller.revealed

    controller.gameManager = gameManagerMemento
    controller.revealed = revealedMemento

    gameManagerMemento = newGameManagerMemento
    revealedMemento = newRevealedMemento
  }

  override def undoStep(): Unit = {
    val newDealerMemento = controller.gameManager.copy()
    val newRevealedMemento : Boolean = controller.revealed

    controller.gameManager = gameManagerMemento
    controller.revealed = revealedMemento

    gameManagerMemento = newDealerMemento
    revealedMemento = newRevealedMemento
    controller.evaluate()
  }
}
