name := """hello"""
organization := "com.github.devcdcc"

version := "1.0-SNAPSHOT"
scalaVersion := "2.13.1"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += ws
lazy val root = (project in file(".")).enablePlugins(PlayScala)
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.github.devcdcc.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.github.devcdcc.binders._"
