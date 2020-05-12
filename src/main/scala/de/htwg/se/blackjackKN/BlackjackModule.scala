package de.htwg.se.blackjackKN

import com.google.inject.AbstractModule
import de.htwg.se.blackjackKN.controller.controllerComponent._
import de.htwg.se.blackjackKN.model.fileioComponent.FileIOInterface
import de.htwg.se.blackjackKN.model.personsComponent._
import de.htwg.se.blackjackKN.model.fileioComponent._
import de.htwg.se.blackjackKN.model.personsComponent
import net.codingwell.scalaguice.ScalaModule

class BlackjackModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[ControllerInterface].to[controllerBaseImpl.Controller]
    bind[FileIOInterface].to[fileioXMLImpl.FileIO]
  }

}
