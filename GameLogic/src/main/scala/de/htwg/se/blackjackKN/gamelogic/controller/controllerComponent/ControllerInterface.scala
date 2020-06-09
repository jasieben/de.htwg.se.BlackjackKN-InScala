package de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent

import de.htwg.se.blackjackKN.gamelogic.model.GameManager
import de.htwg.se.blackjackKN.gamelogic.util.Observable

trait ControllerInterface extends Observable {
  def playerManagementServiceUrl: String

  def startGame(): Unit

  def gameManager: GameManager

  def undo(): Unit

  def redo(): Unit

  def hitCommand(playerId:Int): Boolean

  def loadGameManager(playerId: Int): Boolean

  def loadNewGameManager(playerId: Int): Unit

  def standCommand(playerId:Int): Boolean

  def setBet(playerId:Int, value: Int): Unit

  def startNewRound(playerId: Int, gameId: Option[Int] = None): Unit

}
