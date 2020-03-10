package com.github.devcdcc

package object config {
  private sealed case class RequestType(value: Int)
  private object RangeRequest extends RequestType(value = 1)
  private object GetByDeviceIdRequest extends RequestType(value = 2)
  private object DeviceIdsRByLanguageRequest extends RequestType(value = 3)

  trait AbstractParameter
  case class RangeParameter(start: Long, end: Long) extends AbstractParameter
  case class GetByDeviceIdParameter(start: Long) extends AbstractParameter
  case class DeviceIdsEmptyParameters() extends AbstractParameter

  class JobConfig(args: Array[String]) {
    private def kindRequest: RequestType = RequestType(args(0).toInt)
    def getParameter: AbstractParameter = kindRequest match {
      case RangeRequest => RangeParameter(args(1).toLong, args(2).toLong)
      case GetByDeviceIdRequest => GetByDeviceIdParameter(args(1).toLong)
      case DeviceIdsRByLanguageRequest => DeviceIdsEmptyParameters()
      case _ => throw new Exception("Wrong kind of request")
    }

  }

}
