package de.htwg.se.blackjackKN.controller

import de.htwg.se.blackjackKN.util.Observable

class Controller extends Observable {
  def startGame() : Unit
  def startNewRound() : String
  def stand() : String
  def hit() : String
  def evaluate() : String
}
