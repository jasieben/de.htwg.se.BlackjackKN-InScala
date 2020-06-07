package de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.blackjackKN.gamelogic.model.GameManager
import de.htwg.se.blackjackKN.gamelogic.util.Command

class HitCommand(controller: Controller) extends Command {
  var gameManagerMemento : GameManager = controller.gameManager
  override def doStep(): Unit = {
    gameManagerMemento = controller.gameManager.copy()

    controller.hit()
  }

  override def redoStep(): Unit = {
    val newGamaManagerMemento = controller.gameManager.copy()

    controller.gameManager = gameManagerMemento

    gameManagerMemento = newGamaManagerMemento
  }

  override def undoStep(): Unit = {
    val newGamaManagerMemento = controller.gameManager.copy()

    controller.gameManager = gameManagerMemento

    gameManagerMemento = newGamaManagerMemento
  }
}
