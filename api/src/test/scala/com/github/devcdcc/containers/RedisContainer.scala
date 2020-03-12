package com.github.devcdcc.containers

import com.dimafeng.testcontainers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait

object RedisContainer {
  private val containerName = "bitnami/redis:latest"
  private val REDIS_PORT = 6379
  val master: GenericContainer = new GenericContainer(containerName).configure {
    container =>
      container.withExposedPorts(REDIS_PORT)
      container.addEnv("ALLOW_EMPTY_PASSWORD", "yes")

  }
}
