package de.htwg.se.blackjackKN.model

case class Player(name: String = "Test") extends Person{
  var balance: Double = 1000
  var bet : Bet = Bet(0)
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
}