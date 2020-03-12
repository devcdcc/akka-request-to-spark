package com.github.devcdcc

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import io.circe.generic.auto._
package object domain {
  case class DataDeviceID(value: Long) extends AnyVal
  case class DataTimestamp(value: Long) extends AnyVal
  case class DataURL(value: String) extends AnyVal
  case class DataCountry(value: String) extends AnyVal
  case class DataLanguage(value: String) extends AnyVal

  case class Navigation(
      device_id: Long,
      timestamp: Long,
      url: String,
      country: String
  )
  implicit val navigationDecoder: Decoder[Navigation] =
    deriveDecoder[Navigation]
  implicit val navigationEncoder: Encoder[Navigation] =
    deriveEncoder[Navigation]

  case class Language(country: String, language: String)
  implicit val languageDecoder: Decoder[Language] =
    deriveDecoder[Language]
  implicit val languageEncoder: Encoder[Language] =
    deriveEncoder[Language]
}
