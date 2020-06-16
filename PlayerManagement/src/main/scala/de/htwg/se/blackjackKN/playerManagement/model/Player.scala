package de.htwg.se.blackjackKN.playerManagement.model

import org.mongodb.scala.bson.annotations.BsonProperty

case class Player(@BsonProperty("_id") id: Option[String] = None,
                  name: String = "Test",
                  balance: Int = 1000, bet: Option[Bet] = None) {

  def changeBalance(newBalance: Int): Player = {
    copy(balance = newBalance)
  }

  override def toString: String = name

  def newBet(value: Int): Player = {
    if (value <= balance) {
      copy(balance = balance - value, bet = Option(Bet(value = value)))
    } else {
      copy(bet = None)
    }
  }
}
