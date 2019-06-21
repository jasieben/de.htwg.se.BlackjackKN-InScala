package de.htwg.se.blackjackKN.model.personsComponent

import de.htwg.se.blackjackKN.model.betComponent.Bet

trait PlayerInterface extends PersonsInterface {
  var balance: Double
  var bet : Bet

  def addBet(bet : Bet) : Boolean
  def getName : String
}
