ThisBuild / name          := "PlayerManagement"
ThisBuild / organization  := "de.htwg.se"
ThisBuild / version       := "0.0.1"
ThisBuild / scalaVersion  := "2.12.4"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "junit" % "junit" % "4.8" % "test"

libraryDependencies += "com.google.inject" % "guice" % "4.2.2"

libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.2"

libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6"
