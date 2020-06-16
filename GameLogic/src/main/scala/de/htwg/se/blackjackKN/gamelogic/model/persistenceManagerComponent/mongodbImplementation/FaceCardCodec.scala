package de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent.mongodbImplementation

import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.cardsBaseImpl.{CardFactory, FaceCard, NumberCard}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.bson.{BsonReader, BsonWriter}

class FaceCardCodec extends Codec[FaceCard]{
  override def encode(writer: BsonWriter, value: FaceCard, encoderContext: EncoderContext): Unit = {
    writer.writeString(value.toString)
  }

  override def getEncoderClass: Class[FaceCard] = classOf[FaceCard]

  override def decode(reader: BsonReader, decoderContext: DecoderContext): FaceCard = {
    val string = reader.readString()
    CardFactory.createFaceCardFromString(string)
  }
}
