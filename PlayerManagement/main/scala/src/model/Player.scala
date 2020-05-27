package model

case class Player(
                   name: String = "Test",
                   hand: List[CardInterface] = List[CardInterface](),
                   balance: Int = 1000, bet: Option[Bet] = Option(Bet(0))) extends PersonsInterface {
  override def toString: String = name



  def newBet(value: Int): Player = {
    if (value <= balance) {
      copy(balance = balance - value, bet = Option(Bet(value)))
    } else {
      copy(bet = None)
    }
  }


}
