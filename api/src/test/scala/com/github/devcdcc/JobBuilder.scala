package com.github.devcdcc

import java.io.File

class JobBuilder {

  def exec(sparkDirectory: String) = {
    val args = Array("--master")
    new ProcessBuilder()
      .directory(new File(sparkDirectory))
      .start()
  }
}
