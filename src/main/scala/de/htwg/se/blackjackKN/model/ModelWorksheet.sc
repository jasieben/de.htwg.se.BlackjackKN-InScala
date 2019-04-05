
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

println("Ist das eine Kartenhand?")
if(field2.card(0).x == 42) {
  println("Ja das ist es!")
}
var z:Array[String] = new Array[String](3)

z(0) = "1"
z(1) = "Benni"
z(2) = "Hallo"

for ( x <- z ) {
  println( x )
}

import scala.math.sqrt

case class Cell(value:Int) {
  def isSet:Boolean = value != 0
}

val cell3= Cell(5)
cell3.isSet

val cell4= Cell(0)
cell4.isSet

case class House(cells:Vector[Cell])

val house = House(Vector(cell3,cell4))

house.cells(0).value
house.cells(0).isSet





