package de.htwg.se.blackjackKN.playerManagement.model.persistenceManagerComponent.mongodbImplementation

import de.htwg.se.blackjackKN.playerManagement.model.EndState
import org.bson.codecs.configuration.{CodecProvider, CodecRegistry}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.bson.{BsonReader, BsonWriter}

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
      EndState.withName(enumValue)
    }


    override def encode(writer: BsonWriter, value: Enumeration#Value, encoderContext: EncoderContext): Unit = {
      writer.writeString(value.toString)
    }

    override def getEncoderClass: Class[Enumeration#Value] = classOf[Enumeration#Value]
  }
}
