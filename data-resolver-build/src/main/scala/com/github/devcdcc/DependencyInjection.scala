package com.github.devcdcc

import java.net.InetSocketAddress
import java.util.concurrent.Executor

import com.github.devcdcc.config.JobConfig
import com.github.devcdcc.controller.NavigationController
import com.github.devcdcc.persistence.RedisConnection
import com.github.devcdcc.services.slector.RequestSelector
import com.softwaremill.macwire._
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
class DependencyInjection(arguments: Array[String]) {
  lazy val config: Config = ConfigFactory.load()
  lazy val loadDefaults = true
  lazy val sparkConf: SparkConf = wire[SparkConf]
    .setAppName("simpleReading")
    .setMaster("local")
  implicit val sparkSession: SparkSession = SparkSession
    .builder()
    .config(sparkConf)
    .getOrCreate()
  lazy val navigationController: NavigationController =
    wire[NavigationController]

  lazy val paramConfig: JobConfig = new JobConfig(arguments)
  lazy val requestSelector: RequestSelector = wire[RequestSelector]
  private lazy val redisAddress: InetSocketAddress = new InetSocketAddress(
    paramConfig.getDBConfig.host,
    paramConfig.getDBConfig.port
  )
  lazy val redisConnection: RedisConnection = wire[RedisConnection]

}
