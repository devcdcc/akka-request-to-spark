package com.github.devcdcc

import java.util.Properties

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf

object ReaderJobMain extends App {

  val conf: Config = ConfigFactory.load("application.conf")
  val url = "jdbc:mysql://[host][:port][/[database]]"
  val sparkConf: SparkConf =
    new SparkConf().setAppName("simpleReading").setMaster("local[2]")

  val props: Properties = new Properties
  props.setProperty("user", "root")
  props.setProperty("password", "1234")
//  props.setProperty("isolationLevel", "READ_UNCOMMITTED")
  val sparkSession: SparkSession = SparkSession
    .builder()
    .config(sparkConf)
    .getOrCreate() //SQLContext(sparkContext) // make sql context

  def processData(): Unit =
    sparkSession.read
      .format("csv")
      .option("header", "true")
      .load("some/path/to/file.csv")
      .write
      .mode("overwrite")
      .jdbc(url, "tablename", null)
  processData()

  sys.addShutdownHook(sparkSession.stop())
}
