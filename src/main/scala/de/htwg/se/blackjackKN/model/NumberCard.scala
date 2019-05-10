package de.htwg.se.blackjackKN.model

case class NumberCard(suit: String = "hearts",rank: Int = 10) extends Card {

   val ranks = List(2, 3, 4, 5, 6, 7, 8, 9, 10)
   val suits = List("hearts", "diamonds", "clubs", "spades")

  require(suits.contains(suit))
  require(ranks.contains(rank))

  val value: Int = rank


}
