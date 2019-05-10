package de.htwg.se.blackjackKN.model

case class FaceCard(suit: Suits.Value = Suits.Clubs, rank : Ranks.Value = Ranks.Ace) extends Card{
  val ranks: List[(Ranks.Value, Int)] = List((Ranks.King, 10),(Ranks.Queen,10), (Ranks.Jack,10), (Ranks.Ace,11))

  require(suits.contains(suit))
  require(ranks.exists(_._1 == rank))

  val value: Int = ranks.find(_._1 == rank).get._2

}