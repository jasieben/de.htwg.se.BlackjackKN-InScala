package de.htwg.se.blackjackKN.model

case class FaceCard(suit: String = "hearts", rank : String = "ace") extends Card{

  val suits : List[String] = List("hearts","diamonds", "clubs", "spades")
  val ranks: List[(String, Int)] = List(("king", 10),("queen",10), ("jack",10), ("ace",11))

  require(suits.contains(suit))
  require(ranks.exists(_._1 == rank))

  val value: Int = ranks.find(_._1 == rank).get._2



}
