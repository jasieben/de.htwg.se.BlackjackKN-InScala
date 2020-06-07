package de.htwg.se.blackjackKN.gamelogic.util

trait Command {
  def doStep():Unit
  def undoStep(): Unit
  def redoStep():Unit
}
