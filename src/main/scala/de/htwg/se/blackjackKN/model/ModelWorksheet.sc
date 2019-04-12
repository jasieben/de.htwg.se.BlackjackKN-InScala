val suits : List[String] = List("hearts","tiles", "clovers", "pikes")
val ranks: List[(String, Int)] = List(("king", 10),("queen",10), ("jack",10), ("ace",11))
val faceCards = for {
  r <- ranks
  s <- suits
} yield (r,s)