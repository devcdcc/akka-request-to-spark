package com.github.devcdcc.containers

import com.dimafeng.testcontainers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.BindMode

import scala.collection.JavaConverters._
object SparkContainer {
  private val containerName = "bde2020/spark-master:2.4.3-hadoop2.7"
  val master: GenericContainer = new GenericContainer(containerName).configure {
    container =>
      container.withExposedPorts(7077)
      container.withExtraHost("spark-master", "127.0.0.1")
//      container.setExtraHosts(List("spark-master").asJava)
      container.waitingFor(Wait.forListeningPort())
      container.addEnv("ENABLE_INIT_DAEMON", "false")
      container.withFileSystemBind("./", "/data", BindMode.READ_WRITE)
//      container.addFileSystemBind("spark", "/spark", BindMode.READ_WRITE)
  }

  lazy val worker: GenericContainer =
    new GenericContainer(containerName).configure { container =>
      container.addLink(master.container, "spark-master")
      container.addEnv("ENABLE_INIT_DAEMON", "false")
    }
//  worker.container.waitingFor(Wait.forHttp("/"))
  //.withCopyFileToContainer();
}
