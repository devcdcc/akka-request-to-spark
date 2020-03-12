package com.github.devcdcc

import io.circe.Json

class JobDefinition(args: Array[String])
    extends DependencyInjection(arguments = args) {
  def execute(): Unit = {
    val requestId = paramConfig.getRequestId
    val result: Json = requestSelector.selection
    redisConnection.setKey(requestId, result.noSpaces)
    sys.addShutdownHook {
      sparkSession.stop()
    }
  }
}
