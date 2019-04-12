package de.htwg.se.blackjackKN.model

case class NumberCard(suit: String = "hearts", rank : String = "10") extends Card {

  private val ranks = List (2, 3, 4, 5, 6, 7, 8, 9, 10)
  private val suits = List("hearts", "spades", "pikes", "clovers")

  require(suits.contains(suit))
  require(ranks.contains(rank.toInt))

  val value : Int = rank.toInt

  override def getCards: List[NumberCard] = {
    val cards: List[NumberCard] = for {
      n <- ranks
      s <- suits
    } yield NumberCard(s, n.toString)
    cards
  }
}
