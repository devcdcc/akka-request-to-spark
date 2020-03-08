import org.scalafmt.sbt.ScalafmtPlugin.autoImport.scalafmtOnCompile
import play.sbt.PlayScala
import sbt.{file, project}
import sbt.Keys.{libraryDependencies, name, organization, scalaVersion, version}
import sbt._
import play.sbt.PlayImport._

import scala.collection.immutable
object ApiBuild {
  name := """hello"""
  organization := "com.github.devcdcc"
  version := "1.0-SNAPSHOT"
  scalaVersion := "2.13.1"
  val dependencies: immutable.Seq[ModuleID] = List(
    SharedDependencies.akkaRemote
  ) ++
    SharedDependencies.akkaHttp ++
    SharedDependencies.circeLibraries ++
    SharedDependencies.akkaTesting ++
    SharedDependencies.test

  lazy val api: Project =
    (project in file("api"))
      .settings(libraryDependencies ++= ApiBuild.dependencies, scalafmtOnCompile := true)
}
