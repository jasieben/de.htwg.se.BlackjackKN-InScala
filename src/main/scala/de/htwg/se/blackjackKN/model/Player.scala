package de.htwg.se.blackjackKN.model

import scala.collection.mutable.ListBuffer

case class Player(name: String = "Test", handList : ListBuffer[Card] = ListBuffer.empty[Card], money : Double = 1000, bets : Bet = Bet(0) ) extends Person{
  var hand : ListBuffer[Card] = handList
  var balance: Double = money
  var bet : Bet = bets

  override def toString:String = name

  def addBet(bet: Bet): Boolean = {
    if (bet.value <= balance) {
      balance -= bet.value
      this.bet = bet
      true
    } else {
      false
    }
  }

  def clearBets() : Unit = {
    bet = Bet(0)
  }

  def copy() : Player = {
    Player(this.name, hand.clone(), this.balance, bet)
  }

  def addCardToHand(card: Card): Card = {
    hand += card
    card
  }

  def clearHand() : Unit = {
    hand.clear()
  }

  def getCard(index : Int): Card = {
    hand(index)
  }

  def getHandSize : Int = {
    hand.size
  }

  def getHandValue : Int = {
    var v : Int = 0
    for {
      i <- hand.indices
    } v += hand(i).value
    v
  }

  def getLastHandCard : Card = {
    hand.last
  }

  def containsCardType(rank: Ranks.Value) : Int = {
    for (i <- hand.indices) {
      if (hand(i).rank == rank) {
        return i
      }
    }
    -1
  }
}