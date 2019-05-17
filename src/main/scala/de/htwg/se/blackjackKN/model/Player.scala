package de.htwg.se.blackjackKN.model

case class Player(name: String) extends Person{
  var balance: Int = 1000
  var bet : Bet = Bet(0)
  override def toString:String = name

  def addBet(bet: Bet): Boolean = {
    if (balance <= bet.value) {
      balance += bet.value
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