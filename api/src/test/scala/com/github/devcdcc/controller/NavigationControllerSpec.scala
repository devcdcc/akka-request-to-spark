package com.github.devcdcc.controller

import java.time._

import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import akka.http.scaladsl.model._
import de.heikoseeberger.akkahttpcirce._
import akka.http.scaladsl.server.Directives._
import FailFastCirceSupport._
import com.github.devcdcc.domain._
import com.github.devcdcc.utils.circe.auto._
import com.github.devcdcc.request._
import io.circe.syntax._
class NavigationControllerSpec extends AnyWordSpec with Matchers with ScalatestRouteTest with NavigationController{
  "NavigationController" can {
    "do GET request to navigation" when {
      "get by date" should {


        "get valid reponse" in {
          val start = LocalDateTime.ofInstant(Instant.ofEpochMilli(1557360113L*1000),ZoneId.systemDefault())
          val end = LocalDateTime.ofInstant(Instant.ofEpochMilli(1557360203000L*1000),ZoneId.systemDefault())
          val json = DateRange(start,end)
          val jsonRequest = ByteString(
            json.asJson.noSpaces
          )
          val expected: List[Navigation] = List(
            Navigation(
              device_id = DataDeviceID(1L),
              timestamp = DataTimestamp(1557360113L),
              url = DataURL("www.web1.com/test1"),
              country = DataCountry("AR")
            ),
            Navigation(
              device_id = DataDeviceID(2L),
              timestamp = DataTimestamp(1557360203L),
              url = DataURL("www.web2.com/"),
              country = DataCountry("CL")
            )
          )
          val postRequest = HttpRequest(
            HttpMethods.GET,
            uri = "/navigation",
            entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))

          postRequest ~> route ~> check {
            val response = responseAs[List[Navigation]]
            response shouldBe expected
          }
        }
      }
      "get by deviceId" should {

      }
      "get devicesIds" should {

      }
    }
  }
}
