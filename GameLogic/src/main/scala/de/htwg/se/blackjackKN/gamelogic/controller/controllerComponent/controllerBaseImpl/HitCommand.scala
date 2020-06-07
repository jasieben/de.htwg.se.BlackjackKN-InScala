package de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.blackjackKN.gamelogic.model.GameManager
import de.htwg.se.blackjackKN.gamelogic.util.Command

class HitCommand(controller: Controller) extends Command {
  var gameManagerMemento : GameManager = controller.gameManager
  var revealedMemento : Boolean = controller.revealed
  override def doStep(): Unit = {
    gameManagerMemento = controller.gameManager.copy()

    revealedMemento = controller.revealed
    controller.hit()
  }

  override def redoStep(): Unit = {
    val newGamaManagerMemento = controller.gameManager.copy()
    val newRevealedMemento : Boolean = controller.revealed

    controller.gameManager = gameManagerMemento
    controller.revealed = revealedMemento

    gameManagerMemento = newGamaManagerMemento
    revealedMemento = newRevealedMemento
  }

  override def undoStep(): Unit = {
    val newGamaManagerMemento = controller.gameManager.copy()
    val newRevealedMemento : Boolean = controller.revealed

    controller.gameManager = gameManagerMemento
    controller.revealed = revealedMemento

    gameManagerMemento = newGamaManagerMemento
    revealedMemento = newRevealedMemento
    controller.evaluate()
  }
}
