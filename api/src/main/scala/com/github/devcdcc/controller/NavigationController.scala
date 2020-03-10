package com.github.devcdcc.controller

import java.io.{BufferedReader, InputStreamReader}
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

import akka.http.scaladsl.model._
import de.heikoseeberger.akkahttpcirce._
import akka.http.scaladsl.server.Directives._
import FailFastCirceSupport._
import akka.http.scaladsl.server.Route
import com.github.devcdcc.domain._
import com.github.devcdcc.utils.circe.auto._
import com.github.devcdcc.request._
import org.apache.spark.launcher.SparkLauncher
import io.circe.syntax._

import scala.collection.mutable.ListBuffer
import scala.util.Try
trait NavigationController {
  lazy val route: Route = path("navigation") {
    get {
      entity(as[DateRange]) {
        case DateRange(start, end) =>
          dateRangeRequestHandler(start, end)
      }
    }
  }

  private def dateRangeRequestHandler(
      start: LocalDateTime,
      end: LocalDateTime
  ) = {
    complete {
      val spark: Process = new SparkLauncher()
        .setAppResource("/my/app.jar")
        //        .setMainClass("my.spark.app.Main")
        .setMaster("localhost:7077")
        .addAppArgs(start.toString, end.toString)
        .setConf(SparkLauncher.DRIVER_MEMORY, "2g")
        .launch();
      val list = getJobResponse(spark)
      spark.waitFor(10, TimeUnit.MINUTES)
      spark.exitValue()
      convertResponseToListNavigation(list)
    }
  }

  private def getJobResponse(spark: Process) = {
    val buffer = ListBuffer[String]()
    val inputStream = spark.getInputStream
    try {
      val isr = new InputStreamReader(inputStream)
      val br = new BufferedReader(isr)
      var line: String = ""
      while ({
        line = br.readLine();
        line != null
      }) {
        buffer.append(line)
      }
    } catch {
      case f: Throwable => f.printStackTrace()
    } finally {
      Try {
        inputStream.close()
      }
    }
    buffer.toList
  }

  private def convertResponseToListNavigation(list: List[String]) = {
    list
      .map(value => {
        val Right(json) = io.circe.parser.parse(value)
        json
      })
      .map(json => {
        val Right(navigation) = json.as[Navigation]
        navigation
      })
  }

}
