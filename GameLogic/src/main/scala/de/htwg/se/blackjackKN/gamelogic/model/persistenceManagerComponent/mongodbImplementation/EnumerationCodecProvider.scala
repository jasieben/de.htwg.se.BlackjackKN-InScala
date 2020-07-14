package de.htwg.se.blackjackKN.gamelogic.model.persistenceManagerComponent.mongodbImplementation

import de.htwg.se.blackjackKN.gamelogic.controller.controllerComponent.GameState
import org.bson.{BsonReader, BsonWriter}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.bson.codecs.configuration.{CodecProvider, CodecRegistry}

object EnumerationCodecProvider extends CodecProvider {
  def isEnumVal[T](clazz: Class[T]): Boolean = {
    // I had to check the class name to know if it is a Enumeration Val or Value, since the   Enumeration#Val class is private
    clazz.getName.equals("scala.Enumeration$Val") ||
      clazz.getName.equals("scala.Enumeration$Value")
  }

  override def get[T](clazz: Class[T], registry: CodecRegistry): Codec[T] = {
    if (isEnumVal(clazz)) {
      EnumerationCodec.asInstanceOf[Codec[T]]
    } else {
      null
    }
  }

  object EnumerationCodec extends Codec[Enumeration#Value] {

    override def decode(reader: BsonReader, decoderContext: DecoderContext): Enumeration#Value = {
      val identifier = reader.getCurrentName

      val enumValue = reader.readString()
      // I used the enumMap indexed with the collection key (identifier) to get the Enumeration class
      GameState.withName(enumValue)
    }


    override def encode(writer: BsonWriter, value: Enumeration#Value, encoderContext: EncoderContext): Unit = {
      writer.writeString(value.toString)
    }

    override def getEncoderClass: Class[Enumeration#Value] = classOf[Enumeration#Value]
  }
}
