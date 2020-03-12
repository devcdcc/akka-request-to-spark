package com.github.devcdcc.config

import com.typesafe.config.{Config, ConfigFactory}

class Configuration(config: Config) {
  def sparkURL: String =
    config.getString("spark.master")
  def mainClass: String = config.getString("spark.mainClass")

}
