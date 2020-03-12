import sbt._

object DataResolverBuild {

  val sparkVersion = "2.4.3"
  lazy val sparkDependencies = Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
    "com.holdenkarau" %% "spark-testing-base" % s"${sparkVersion}_0.12.0" % Test,
    "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
  )
  lazy val dependencies: Seq[ModuleID] =
    sparkDependencies ++
      SharedDependencies.circeLibraries ++
      SharedDependencies.macWireDependencies :+
      SharedDependencies.configDependency :+
      SharedDependencies.jodaLibrary :+
      SharedDependencies.akkaRemote :+
//      SharedDependencies.betterFiles :+
      SharedDependencies.redis

}
