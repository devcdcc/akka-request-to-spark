import sbt.Keys.{
  baseDirectory,
  fork,
  fullClasspath,
  isSnapshot,
  javaOptions,
  javacOptions,
  libraryDependencies,
  mainClass,
  name,
  organization,
  parallelExecution,
  pomIncludeRepository,
  publishTo,
  resolvers,
  run,
  runner,
  scalaVersion,
  scalacOptions,
  unmanagedBase,
  version
}
import sbt._

object SharedDependencies {

  lazy val configDependency = "com.typesafe" % "config" % "1.3.3"
  private val akkaVersion = "2.5.26"
  val akkaRemote: ModuleID = "com.typesafe.akka" %% "akka-remote" % akkaVersion
  val akkaTesting = Seq(
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
    "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test
  )
  val akkaHttp: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-http" % "10.1.11",
    "com.typesafe.akka" %% "akka-stream" % akkaVersion
  )
  private val circeVersion = "0.12.0"
  lazy val circeLibraries: Seq[ModuleID] =
    Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-generic-extras",
      "io.circe" %% "circe-parser",
      "io.circe" %% "circe-optics"
    ).map(_ % circeVersion)
  val jodaVersion = "2.10.5"
  lazy val jodaLibrary = "joda-time" % "joda-time" % jodaVersion

  val macWireVersion = "2.3.3"

  lazy val macWireDependencies = Seq(
    "com.softwaremill.macwire" %% "macros" % macWireVersion % "provided",
    "com.softwaremill.macwire" %% "macrosakka" % macWireVersion % "provided",
    "com.softwaremill.macwire" %% "util" % macWireVersion,
    "com.softwaremill.macwire" %% "proxy" % macWireVersion
  )

  lazy val test = Seq(
    "org.scalactic" %% "scalactic" % "3.1.0",
    "org.scalatest" %% "scalatest" % "3.1.0" % Test,
    "org.mockito" %% "mockito-scala-scalatest" % "1.11.2" % Test
  )
}