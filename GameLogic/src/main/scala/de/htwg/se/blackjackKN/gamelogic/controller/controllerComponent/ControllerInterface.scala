package de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent

import GameState.GameState
import de.htwg.se.blackjackKN.gamelogic.util.Observable

trait ControllerInterface extends Observable {
  def createNewPlayer(name: String): Unit

  def startGame(): Unit

  def gameStates: List[GameState]

  def undo(): Unit

  def redo(): Unit

  def hitCommand(): Unit

  def standCommand(): Unit

  def setBet(value: Int): Boolean

  def startNewRound(): Unit

  def changePlayer(name: String): Unit

}
