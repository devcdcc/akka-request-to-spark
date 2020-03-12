package com.github.devcdcc

import io.circe.Json
import io.circe.syntax._
import io.circe.parser._
import io.circe.generic.auto._

package object config {
  private sealed case class RequestType(value: Int)
  private object RangeRequest extends RequestType(value = 1)
  private object GetByDeviceIdRequest extends RequestType(value = 2)
  private object DeviceIdsRByLanguageRequest extends RequestType(value = 3)

  trait AbstractParameter
  case class RangeParameter(start: Long, end: Long) extends AbstractParameter
  case class GetByDeviceIdParameter(device_id: Long) extends AbstractParameter
  case class DeviceIdsEmptyParameters() extends AbstractParameter
  case class DBAddress(host: String, port: Int)

  class JobConfig(args: Array[String]) {
    private def kindRequest: RequestType = RequestType(args(0).toInt)
    def getParameter: AbstractParameter = kindRequest match {
      case RangeRequest =>
        val Right(range) = decode[RangeParameter](args(1))
//        RangeParameter(args(1).toLong, args(2).toLong)
        range
      case GetByDeviceIdRequest =>
        val Right(value) = decode[GetByDeviceIdParameter](args(1))
        value
      case DeviceIdsRByLanguageRequest => DeviceIdsEmptyParameters()
      case _ => throw new Exception("Wrong kind of request")
    }
    def getRequestId: String = args(2)
    def getDBConfig: DBAddress = {
      val Right(value) = decode[DBAddress](args(3))
      value
    }
  }

}
