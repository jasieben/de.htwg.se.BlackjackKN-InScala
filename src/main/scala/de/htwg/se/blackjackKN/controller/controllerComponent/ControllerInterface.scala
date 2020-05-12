package de.htwg.se.blackjackKN.controller.controllerComponent

import GameState.GameState
import de.htwg.se.blackjackKN.model.personsComponent.{Dealer, Player}
import de.htwg.se.blackjackKN.util.Observable

trait ControllerInterface extends Observable {
  def createNewPlayer(name: String): Unit

  def startGame(): Unit

  var dealer: Dealer

  var player: Player

  def gameStates: List[GameState]

  def undo(): Unit

  def redo(): Unit

  def hitCommand(): Unit

  def standCommand(): Unit

  def setBet(value: Int): Boolean

  def startNewRound(): Unit

  def changePlayer(name: String): Unit

}
