package de.htwg.se.blackjackKN.util

import de.htwg.se.blackjackKN.controller.ControllerBaseImpl.Controller

trait Command {
  def doStep():Unit
  def undoStep(): Unit
  def redoStep():Unit
}
