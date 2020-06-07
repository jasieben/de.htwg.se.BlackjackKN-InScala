package de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent

import de.htwg.se.blackjackKN.gamelogic.model.GameManager
import de.htwg.se.blackjackKN.gamelogic.util.Observable

trait ControllerInterface extends Observable {
  def playerManagementServiceUrl: String

  def startGame(): Unit

  def gameManager: GameManager

  def undo(): Unit

  def redo(): Unit

  def hitCommand(): Unit

  def standCommand(): Unit

  def setBet(value: Int): Unit

  def startNewRound(): Unit

}
