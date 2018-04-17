name := "Chat Range Exporter"

version := "0.1"

scalaVersion := "2.12.4"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint")

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "com.github.gilbertw1" %% "slack-scala-client" % "0.2.3"
libraryDependencies += "com.nulab-inc" % "backlog4j" % "2.2.1"
