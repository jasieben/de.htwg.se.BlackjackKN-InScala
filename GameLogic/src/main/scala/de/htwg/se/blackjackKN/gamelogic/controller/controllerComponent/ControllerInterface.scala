package de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent

import de.htwg.se.blackjackKN.gamelogic.model.GameManager
import de.htwg.se.blackjackKN.gamelogic.util.Observable

trait ControllerInterface extends Observable {
  def currentPlayerIndex: Int

  def playerManagementServiceUrl: String

  def startGame(): Unit

  def gameManager: GameManager

  def undo(): Unit

  def redo(): Unit

  def hitCommand(playerId: String): Boolean

  def loadGameManager(playerId: String): Boolean

  def loadNewGameManager(playerId: String): Unit

  def standCommand(playerId: String): Boolean

  def setBet(playerId: String, value: Int): Unit

  def removePlayerFromGame(playerId: String): Unit

  def startNewRound(playerId: String, gameId: Option[String] = None): Unit

}
