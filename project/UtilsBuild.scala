import sbt._
object UtilsBuild {
  lazy val dependencies: Seq[ModuleID] =
    SharedDependencies.circeLibraries ++
      SharedDependencies.test :+
      SharedDependencies.configDependency :+
      SharedDependencies.jodaLibrary
}
