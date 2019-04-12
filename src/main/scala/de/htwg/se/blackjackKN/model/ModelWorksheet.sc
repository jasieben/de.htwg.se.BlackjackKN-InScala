val nums = Seq(1,2,3, 4, 5, 6, 7, 8, 9, 10)
val suits = Seq("hearts", "spades", "pikes", "clovers")
val cards = for {
  n <- nums
  s <- suits
} yield (n, s)
for (n <- cards) {
  println(n)
}

