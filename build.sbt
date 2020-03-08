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
scalafmtOnCompile := true
lazy val utils = {
  (project in file("utils"))
    .settings(libraryDependencies ++= UtilsBuild.dependencies)
}
lazy val `data-resolver-build` = {
  (project in file("data-resolver-build"))
    .settings(
      libraryDependencies ++= DataResolverBuild.dependencies,
      mainClass in assembly := Some("io.github.devcdcc.Main")
    )
    .dependsOn(utils)
    .aggregate(utils)
}
val api = ApiBuild.api
  .dependsOn(utils)
  .aggregate(utils)
parallelExecution in Test := false
