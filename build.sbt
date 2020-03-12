name := "retargetly"

version := "0.1"

scalaVersion := "2.12.10"
Compile / resourceDirectory := baseDirectory.value / "resources"
Test / resourceDirectory := baseDirectory.value / "resources"
fork in Test := true
javaOptions ++= Seq(
  "-Xms512M",
  "-Xmx2048M",
  "-XX:MaxPermSize=2048M",
  "-XX:+CMSClassUnloadingEnabled"
)
lazy val utils = {
  (project in file("utils"))
    .settings(
//      crossScalaVersions := List("2.12.10", "2.11.12"),
      libraryDependencies ++= UtilsBuild.dependencies,
      scalafmtOnCompile := true
    )
}
lazy val `data-resolver-build` = {
  (project in file("data-resolver-build"))
    .settings(
      scalacOptions += "-target:jvm-1.8",
      assemblyMergeStrategy in assembly := {
        case x if x.contains("io.netty.versions.properties") =>
          MergeStrategy.concat
        case x =>
          val oldStrategy = (assemblyMergeStrategy in assembly).value
          oldStrategy(x)
      },
      scalaVersion := "2.11.12",
      libraryDependencies ++= DataResolverBuild.dependencies,
      mainClass in assembly := Some("io.github.devcdcc.ReaderJobMain"),
      scalafmtOnCompile := true
    )
}
val api = ApiBuild.api
  .dependsOn(utils)
  .aggregate(utils)
parallelExecution in Test := false
