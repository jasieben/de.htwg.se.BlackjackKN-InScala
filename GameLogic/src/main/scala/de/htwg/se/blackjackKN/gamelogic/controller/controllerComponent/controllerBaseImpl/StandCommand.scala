package de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.blackjackKN.gamelogic.model.GameManager
import de.htwg.se.blackjackKN.gamelogic.util.Command

class StandCommand(controller: Controller) extends Command {
  var gameManagerMemento : GameManager = controller.gameManager
  override def doStep(): Unit = {
    gameManagerMemento = controller.gameManager.copy()
    controller.stand()
  }

  override def redoStep(): Unit = {
    val newGameManagerMemento = controller.gameManager.copy()

    controller.gameManager = gameManagerMemento

    gameManagerMemento = newGameManagerMemento
  }

  override def undoStep(): Unit = {
    val newDealerMemento = controller.gameManager.copy()

    controller.gameManager = gameManagerMemento

    gameManagerMemento = newDealerMemento
    controller.evaluate()
  }
}
