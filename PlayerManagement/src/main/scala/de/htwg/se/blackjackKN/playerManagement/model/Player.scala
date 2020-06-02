package de.htwg.se.blackjackKN.playerManagement.model

case class Player(id: Int = 5,
                  name: String = "Test",
                  balance: Int = 1000, bet: Option[Bet] = Option(Bet(0))) {

  def changeBalance(newBalance: Int): Player = {
    copy(balance = newBalance)
  }

  override def toString: String = name

  def newBet(value: Int): Player = {
    if (value <= balance) {
      copy(balance = balance - value, bet = Option(Bet(value)))
    } else {
      copy(bet = None)
    }
  }
}
