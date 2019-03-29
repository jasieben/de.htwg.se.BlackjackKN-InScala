
case class Carddeck(x:Int, y:Int)

val cell1 = Carddeck(4,5)
cell1.x
cell1.y

val cell2 = Carddeck(42, 3)
cell2.x
cell2.y

case class Hand(card: Array[Carddeck])

val field1 = Hand(Array.ofDim[Carddeck](1))
val field2 = Hand(Array.ofDim[Carddeck](1))
field1.card(0)=cell1
field1.card(0).x
field1.card(0).y
field2.card(0)=cell2
field2.card(0).x
field2.card(0).y

