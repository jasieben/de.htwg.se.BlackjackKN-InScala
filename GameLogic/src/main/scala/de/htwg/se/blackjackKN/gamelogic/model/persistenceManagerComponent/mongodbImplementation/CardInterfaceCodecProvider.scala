package de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent.mongodbImplementation

import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.CardInterface
import de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.cardsBaseImpl.CardFactory
import de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent.mongodbImplementation.EnumerationCodecProvider.EnumerationCodec
import org.bson.codecs.configuration.{CodecProvider, CodecRegistry}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.bson.{BsonReader, BsonReaderMark, BsonType, BsonWriter}

import scala.util.Try

object CardInterfaceCodecProvider extends CodecProvider {

  object CardInterfaceCodec extends Codec[CardInterface] {
    override def encode(writer: BsonWriter, value: CardInterface, encoderContext: EncoderContext): Unit = {
      writer.writeString(value.toString)
    }

    override def getEncoderClass: Class[CardInterface] = classOf[CardInterface]

    override def decode(reader: BsonReader, decoderContext: DecoderContext): CardInterface = {
      reader.readStartDocument()
      var isLowValueAce: Option[Boolean] = None
      var rankString = ""
      var rankInt = -1
      val suit = reader.readString("suit")
      if (reader.readBsonType() == BsonType.STRING) {
        rankString = reader.readString()
      } else {
        rankInt = reader.readInt32()
      }

      isLowValueAce = if (reader.readBsonType() != BsonType.END_OF_DOCUMENT) Some(reader.readBoolean()) else None
      reader.readEndDocument()
      CardFactory.createFromJson(rankString, rankInt, suit, isLowValueAce)
    }
  }

  override def get[T](clazz: Class[T], registry: CodecRegistry): Codec[T] = {
    if (clazz.getName.equals("de.htwg.se.blackjackKN.gamelogic.model.cardsComponent.CardInterface")) {
      CardInterfaceCodec.asInstanceOf[Codec[T]]
    } else {
      null
    }
  }
}
