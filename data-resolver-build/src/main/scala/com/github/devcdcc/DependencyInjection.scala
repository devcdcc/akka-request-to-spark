package com.github.devcdcc

import com.github.devcdcc.controller.NavigationController
import com.github.devcdcc.services.slector.RequestSelector
import com.softwaremill.macwire._
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
trait DependencyInjection {
  lazy val conf: Config = ConfigFactory.load()
  lazy val loadDefaults = true
  lazy val sparkConf: SparkConf = wire[SparkConf]
    .setAppName("simpleReading")
    .setMaster("local[2]")
  implicit val sparkSession: SparkSession = SparkSession
    .builder()
    .config(sparkConf)
    .getOrCreate()
  lazy val navigationController: NavigationController =
    wire[NavigationController]

  def arguments: Array[String]
  lazy val paramConfig: config.JobConfig = new config.JobConfig(arguments)
  lazy val requestSelector: RequestSelector = wire[RequestSelector]

}
