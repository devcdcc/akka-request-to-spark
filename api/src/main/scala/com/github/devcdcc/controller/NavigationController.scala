package com.github.devcdcc.controller

import java.io.{BufferedReader, InputStreamReader}
import java.util
import java.util.concurrent.TimeUnit
import java.util.Properties
import java.util.logging.Logger

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.devcdcc.config.Configuration
import com.github.devcdcc.domain._
import com.github.devcdcc.idhandler.RequestIdGenerator
import com.github.devcdcc.persistence.RedisConnection
import com.github.devcdcc.request._
import com.github.devcdcc.utils.circe.auto._
import de.heikoseeberger.akkahttpcirce._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.syntax._
import org.apache.spark.launcher.{SparkAppHandle, SparkLauncher}

import scala.collection.JavaConverters._
class NavigationController(
    controllerConfiguration: Configuration,
    requestIdGenerator: RequestIdGenerator,
    redisConnection: RedisConnection
) extends SparkAppHandle.Listener {
  private val jarPath = "./data-resolver-build-assembly-1.jar"
  private val logger = Logger.getGlobal
  private val dbAddress = DBAddress(
    redisConnection.address.getHostName,
    redisConnection.address.getPort
  )
  private def sparkHome = "/home/devcdcc/.programs/spark-2.4.3-bin-hadoop2.7"
  lazy val route: Route = path("navigation") {
    get {
      entity(as[DateRange]) { dateRange: DateRange =>
        dateRangeRequestHandler(dateRange)
      }
    }
  } ~ pathPrefix("navigation" / LongNumber) { device_id =>
    navigationOfDeviceRequestHandler(NavigationOfDeviceId(device_id))
  }
  private def dateRangeRequestHandler(
      dateRange: DateRange
  ) = {
    complete {
      val requestId = requestIdGenerator.generate
      val requestText = dateRange.asJson.noSpaces
      val sparkUrl = controllerConfiguration.sparkURL
      val mainClass = controllerConfiguration.mainClass
      val dbArg = dbAddress.asJson.noSpaces
      val argsJob = Array("1", requestText, requestId, dbArg)
      val spark = new SparkLauncher()
        .setAppResource(jarPath)
        .setDeployMode("cluster")
        .setSparkHome(sparkHome)
        .setMainClass(mainClass)
        .setMaster(sparkUrl)
        .addAppArgs(argsJob: _*)
        .setVerbose(true)
        .setConf(SparkLauncher.DRIVER_MEMORY, "2G")
        .startApplication(this)
      //        .launch()
      //      spark.waitFor(1, TimeUnit.HOURS)
      Thread.sleep(1000 * 120)
      val value = redisConnection.get(requestId).getOrElse("")
      val Right(navigationList) =
        io.circe.parser.decode[List[Navigation]](value)
      navigationList
    }
  }
  private def navigationOfDeviceRequestHandler(
      dateRange: NavigationOfDeviceId
  ) = {
    complete {
      val requestId = requestIdGenerator.generate
      val requestText = dateRange.asJson.noSpaces
      val sparkUrl = controllerConfiguration.sparkURL
      val mainClass = controllerConfiguration.mainClass
      val dbArg = dbAddress.asJson.noSpaces
      val argsJob = Array("2", requestText, requestId, dbArg)
      val spark = new SparkLauncher()
        .setAppResource(jarPath)
        .setDeployMode("cluster")
        .setSparkHome(sparkHome)
        .setMainClass(mainClass)
        .setMaster(sparkUrl)
        .addAppArgs(argsJob: _*)
        .setVerbose(true)
        .setConf(SparkLauncher.DRIVER_MEMORY, "2G")
        .startApplication(this)
      //        .launch()
      //      spark.waitFor(1, TimeUnit.HOURS)
      Thread.sleep(1000 * 120)
      val value = redisConnection.get(requestId).getOrElse("")
      val Right(navigationList) =
        io.circe.parser.decode[List[Navigation]](value)
      navigationList
    }
  }
  override def infoChanged(handle: SparkAppHandle): Unit = {
    logger.info(
      "Spark App Id [" + handle.getAppId + "] Info Changed.  State [" + handle.getState + "]"
    )
  }
  override def stateChanged(handle: SparkAppHandle): Unit =
    logger.info(
      "Spark App Id [" + handle.getAppId + "] Info Changed.  State [" + handle.getState + "]"
    )
}
