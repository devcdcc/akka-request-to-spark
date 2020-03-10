package com.github.devcdcc

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

import scala.collection.JavaConverters._
object SparkContainer {
  private val containerName = "bde2020/spark-master:2.4.0-hadoop2.8"
  val master = new GenericContainer(containerName)
  master.withExposedPorts(7077, 8080, 6066)
  master.setExtraHosts(List("spark-master").asJava)
  master.waitingFor(Wait.forHttp("/"))
  master.addEnv("ENABLE_INIT_DAEMON", "false")

  private lazy val worker = new GenericContainer(containerName)
  worker
    .addLink(master, "spark-master:spark-master")
  worker
    .addEnv("ENABLE_INIT_DAEMON", "false")
  //.withCopyFileToContainer();
}
