package de.htwg.se.blackjackKN.model

case class Player(name: String, balance: Int = 0) extends Person{


  override def toString:String = name

}