package de.htwg.se.blackjackKN.playerManagement

import com.google.inject.AbstractModule
import de.htwg.se.blackjackKN.playerManagement.model.fileioComponent.FileIOInterface
import de.htwg.se.blackjackKN.playerManagement.model.fileioComponent.fileioJSONImpl.FileIO
import de.htwg.se.blackjackKN.playerManagement.model.fileioComponent._
import de.htwg.se.blackjackKN.playerManagement.model.persistenceManagerComponent.PlayerPersistenceInterface
import de.htwg.se.blackjackKN.playerManagement.model.persistenceManagerComponent.slickImplementation.PlayerPersistence
import net.codingwell.scalaguice.ScalaModule

class PlayerManagmentModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[FileIOInterface].to[FileIO]
    bind[PlayerPersistenceInterface].to[PlayerPersistence]
  }

}
