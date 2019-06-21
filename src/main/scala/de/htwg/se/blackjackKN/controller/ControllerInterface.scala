package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.controller.GameState
import de.htwg.se.blackjackKN.controller.GameState.GameState
import de.htwg.se.blackjackKN.model.{Dealer, Player}
import de.htwg.se.blackjackKN.util.Observable



trait ControllerInterface extends Observable {
  def createNewPlayer(name: String): Unit
  def startGame() : Unit
  def dealer:Dealer
  def player:Player
  def gameStates:List[GameState]
  def undo(): Unit
  def redo(): Unit
  def hitCommand(): Unit
  def standCommand(): Unit
  def setBet(value: Int): Boolean
  def startNewRound(): Unit

}
