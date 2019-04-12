package de.htwg.se.blackjackKN.model

case class FaceCard() {
  private val suits : List[String] = List("hearts","tiles", "clovers", "pikes")
  private val ranks: List[(String, Int)] = List(("king", 10),("queen",10), ("jack",10), ("ace",11))
  val faceCards: List[((String, Int), String)] = for {
    r <- ranks
    s <- suits
  } yield (r,s)
}
