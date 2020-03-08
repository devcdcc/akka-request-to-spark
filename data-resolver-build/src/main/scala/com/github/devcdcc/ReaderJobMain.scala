package com.github.devcdcc

import com.github.devcdcc.services.sender.AkkaSubmission
import io.circe.Json
object ReaderJobMain extends DependencyInjection with App {
  override lazy val arguments: Array[String] = args
  val result: Json = requestSelector.selection
  AkkaSubmission.send(result)
  sys.addShutdownHook {
    sparkSession.stop()
    AkkaSubmission.system.terminate().wait()
  }
}
