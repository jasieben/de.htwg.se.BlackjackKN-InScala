package de.htwg.se.blackjackKN.model

case class NumberCard() {
  private val nums = List (2, 3, 4, 5, 6, 7, 8, 9, 10)
  private val suits = List("hearts", "spades", "pikes", "clovers")
  val numcards: List[(Int, String)] = for {
    n <- nums
    s <- suits
  } yield (n, s)

}
