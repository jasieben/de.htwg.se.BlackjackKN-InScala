package de.htwg.se.blackjackKN.model

import scala.collection.mutable.ListBuffer

case class Player(name: String, balance: Int = 0) extends Person{


  override def toString:String = name

}