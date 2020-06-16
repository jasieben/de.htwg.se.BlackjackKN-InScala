package de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent.mongodbImplementation

import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.cardsBaseImpl.CardFactory
import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.cardsBaseImpl.NumberCard
import org.bson.{BsonReader, BsonWriter}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}

class NumberCardCodec extends Codec[NumberCard]{
  override def encode(writer: BsonWriter, value: NumberCard, encoderContext: EncoderContext): Unit = {
    writer.writeString(value.toString)
  }

  override def getEncoderClass: Class[NumberCard] = classOf[NumberCard]

  override def decode(reader: BsonReader, decoderContext: DecoderContext): NumberCard = {
    val string = reader.readString()
    CardFactory.createNumberCardFromString(string)
  }
}
