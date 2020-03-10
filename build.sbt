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
//      crossScalaVersions := List("2.12.10", "2.11.12"),
      libraryDependencies ++= DataResolverBuild.dependencies,
      mainClass in assembly := Some("io.github.devcdcc.ReaderJobMain"),
      scalafmtOnCompile := true
    )
    .dependsOn(utils)
    .aggregate(utils)
}
val api = ApiBuild.api
  .dependsOn(utils)
  .aggregate(utils)
parallelExecution in Test := false
