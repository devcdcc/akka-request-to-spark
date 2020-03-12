package com.github.devcdcc.controller

import java.net.InetSocketAddress
import java.time._
import java.util.{Properties, UUID}

import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}
import akka.http.scaladsl.model._
import de.heikoseeberger.akkahttpcirce._
import FailFastCirceSupport._
import com.dimafeng.testcontainers.{
  Container,
  ForAllTestContainer,
  GenericContainer,
  MultipleContainers
}
import com.dimafeng.testcontainers.scalatest.TestContainersForAll
import com.github.devcdcc.config.Configuration
import com.github.devcdcc.domain._
import com.github.devcdcc.utils.circe.auto._
import com.github.devcdcc.request._
import io.circe.syntax._
import io.circe.Json
import com.github.devcdcc.containers.{RedisContainer, SparkContainer}
import com.github.devcdcc.idhandler.RequestIdGenerator
import com.github.devcdcc.persistence.RedisConnection
import com.softwaremill.macwire.wire
import com.typesafe.config.{Config, ConfigFactory}

import collection.JavaConverters._
import org.testcontainers.containers.wait.strategy.Wait
import org.mockito.scalatest.MockitoSugar
import org.testcontainers.containers.Network
class NavigationControllerSpec
    extends AnyWordSpecLike
    with Matchers
    with ScalatestRouteTest
    with ForAllTestContainer
    with MockitoSugar {

  val controllerConfiguration: Configuration = mock[Configuration]

  lazy val subject = new NavigationController(
    controllerConfiguration,
//    subjectProperties,
    requestIdGenerator,
    redisConnection
  )
  val requestIdGenerator: RequestIdGenerator = mock[RequestIdGenerator]

  val network: Network = Network.SHARED
  private lazy val redisAddress: InetSocketAddress =
    new InetSocketAddress(
      RedisContainer.master.container.getContainerInfo.getNetworkSettings.getNetworks.asScala.values.head.getIpAddress,
      6379
    )
  lazy val redisConnection: RedisConnection = wire[RedisConnection]

  when(requestIdGenerator.generate) thenReturn "123"
  override val container: Container = {
    Seq(SparkContainer.master, SparkContainer.worker, RedisContainer.master)
      .foreach(container =>
        container.configure(container => container.setNetwork(network))
      )
    MultipleContainers(
      SparkContainer.master,
      SparkContainer.worker,
      RedisContainer.master
    )
  }

  "NavigationController" can {
    "do GET request to navigation" when {
      "get by date" should {

        "get valid reponse" in {
          //given
          val start = 1557360113L
          val end = 1557360203L
          val json = DateRange(start, end)
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
            entity = HttpEntity(MediaTypes.`application/json`, jsonRequest)
          )

          //when
          when(controllerConfiguration.mainClass) thenReturn "com.github.devcdcc.ReaderJobMain"
          when(controllerConfiguration.sparkURL) thenReturn s"spark://${SparkContainer.master.container.getContainerInfo.getNetworkSettings.getNetworks.asScala.values.head.getIpAddress}:7077"
          postRequest ~> subject.route ~> check {
            //then
            val response = responseAs[String] //As[List[Navigation]]
            io.circe.parser.parse(response) shouldBe Right(expected.asJson)
          }

        }
      }
      "get by deviceId" should {}
      "get devicesIds" should {}
    }
  }
  val requestId: String = requestIdGenerator.generate
//  lazy val subjectProperties: Properties = {
//    val props = new Properties()
//    props.setProperty(
//      "DB_HOST",
//      RedisContainer.master.container.getContainerInfo.getNetworkSettings.getNetworks.asScala.values.head.getGateway
//    )
//    props.setProperty(
//      "DB_PORT",
//      RedisContainer.master.container.getMappedPort(6379).toString
//    )
//    props.setProperty(
//      "REQUEST_ID",
//      requestId
//    )
//    props
//  }

}
