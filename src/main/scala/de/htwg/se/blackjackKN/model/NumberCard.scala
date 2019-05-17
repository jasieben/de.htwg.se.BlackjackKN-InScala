package de.htwg.se.blackjackKN.model

case class NumberCard(suit: Suits.Value = Suits.Spades ,rank: Int = 10) extends Card {

   val ranks = List(2, 3, 4, 5, 6, 7, 8, 9, 10)

  require(suits.contains(suit))
  require(ranks.contains(rank))

  var value: Int = rank


}
