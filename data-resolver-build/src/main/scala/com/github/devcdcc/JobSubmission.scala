package com.github.devcdcc

import org.apache.spark.launcher.SparkLauncher
object JobSubmission {
  val spark: Process = new SparkLauncher()
    .setAppResource("/my/app.jar")
    .setMainClass("my.spark.app.Main")
    .setMaster("local")
    .addAppArgs()
    .setConf(SparkLauncher.DRIVER_MEMORY, "2g")
    .launch();
  spark.waitFor();
}
