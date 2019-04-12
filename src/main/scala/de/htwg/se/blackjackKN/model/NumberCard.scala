package de.htwg.se.blackjackKN.model

case class NumberCard() {
  val nums = List (1,2,3, 4, 5, 6, 7, 8, 9, 10)
  val suits = List("hearts", "spades", "pikes", "clovers")
  val numcards: List[(Int, String)] = for {
    n <- nums
    s <- suits
  } yield (n, s)

}
