package com.github.devcdcc

import java.time.LocalDateTime

package object request {
  case class DateRange(start: Long, end: Long)

  case class NavigationOfDeviceId(device_id: Long)
  case class DBAddress(host: String, port: Int)
}
