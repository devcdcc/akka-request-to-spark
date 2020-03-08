package com.github.devcdcc

import io.circe.{Decoder, Encoder}
import com.github.devcdcc.utils.circe.auto._
import io.circe.generic.semiauto._
package object domain {
  case class DataDeviceID(value: Long) extends AnyVal
  case class DataTimestamp(value: Long) extends AnyVal
  case class DataURL(value: String) extends AnyVal
  case class DataCountry(value: String) extends AnyVal
  case class DataLanguage(value: String) extends AnyVal

  case class Navigation(device_id: DataDeviceID,
                        timestamp: DataTimestamp,
                        url: DataURL,
                        country: DataCountry)
  implicit val navigationDecoder: Decoder[Navigation] =
    deriveDecoder[Navigation]
  implicit val navigationEncoder: Encoder[Navigation] =
    deriveEncoder[Navigation]

  case class Language(country: DataCountry, language: DataLanguage)
  implicit val languageDecoder: Decoder[Language] =
    deriveDecoder[Language]
  implicit val languageEncoder: Encoder[Language] =
    deriveEncoder[Language]
}
