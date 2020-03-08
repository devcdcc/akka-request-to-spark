package com.github.devcdcc

import java.time.LocalDateTime

package object request {
  case class DateRange(start: LocalDateTime, end: LocalDateTime)
  case class NavigationOfDeviceId(id: Long)
}
