package de.htwg.se.blackjackKN.playerManagement

import com.google.inject.AbstractModule
import de.htwg.se.blackjackKN.playerManagement.model.persistenceManagerComponent.PlayerPersistenceInterface
import de.htwg.se.blackjackKN.playerManagement.model.persistenceManagerComponent.mongodbImplementation.PlayerPersistence
import net.codingwell.scalaguice.ScalaModule

class PlayerManagmentModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[PlayerPersistenceInterface].to[PlayerPersistence]
  }

}
