package de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.cardsBaseImpl

import de.htwg.se.blackjackKN.gamelogic.model.{Ranks, Suits}

case class NumberCard(suit: Suits.Value = Suits.Spades, rank: Int = 10) extends Card {

  val ranks = List(2, 3, 4, 5, 6, 7, 8, 9, 10)

  require(suits.contains(suit))
  require(ranks.contains(rank))

  lazy val value: Int = rank
}
