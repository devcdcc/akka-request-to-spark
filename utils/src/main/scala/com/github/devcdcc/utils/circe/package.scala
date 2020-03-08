package com.github.devcdcc.utils

import java.net.InetAddress
import java.time.{Instant, LocalDateTime}

import io.circe._
import io.circe.generic.extras.{AutoDerivation, Configuration}

import scala.util.{Failure, Success, Try}

package object circe {

  object auto extends AutoDerivation {

    import enum._
    import shapeless._

    implicit def encoderValueClass[T <: AnyVal, V](
      implicit g: Lazy[Generic.Aux[T, V :: HNil]],
      e: Encoder[V]
    ): Encoder[T] = Encoder.instance { value ⇒
      e(g.value.to(value).head)
    }

    implicit def decoderValueClass[T <: AnyVal, V](
      implicit g: Lazy[Generic.Aux[T, V :: HNil]],
      d: Decoder[V]
    ): Decoder[T] = {
      val value = Decoder.instance { cursor ⇒
        {
          val result = d(cursor) match {
            case Right(value) => g.value.from(value :: HNil)
            case _            => null
          }
          Right(result.asInstanceOf[T])
        }
      }
      value
    }

    import org.joda.time.DateTime

    implicit lazy val localDateTimeEncoder: Encoder[LocalDateTime] =
      (localDateTime: LocalDateTime) => Json.fromString(localDateTime.toString)

    implicit lazy val localDateTimeDecoder: Decoder[LocalDateTime] =
      (c: HCursor) => {
        val failResult = Left(DecodingFailure("LocalDateTime", c.history))
        c.value.asString
          .map(
            value =>
              Try(LocalDateTime.parse(value)) match {
                case Success(value) => Right(value)
                case Failure(_) =>
                  Try(DateTime.parse(value))
                    .map(convertJODADateTimeToJavaZonedDateTime)
                    .fold(_ => failResult, dateTime => Right(dateTime))
            }
          )
          .getOrElse(failResult)
      }

    private def convertJODADateTimeToJavaZonedDateTime(
      originDateTime: DateTime
    ): LocalDateTime =
      java.time.ZonedDateTime
        .ofInstant(
          Instant.ofEpochMilli(originDateTime.getMillis),
          java.time.ZoneId.of(originDateTime.getZone.getID)
        )
        .toLocalDateTime

    implicit lazy val inetAddressEncoder: Encoder[InetAddress] =
      (address: InetAddress) => Json.fromString(address.getHostAddress)
    implicit lazy val inetAddressDecoder: Decoder[InetAddress] = (c: HCursor) =>
      Try(
        c.value.asString
          .map(string => InetAddress.getByName(string))
      ) match {
        case Failure(exception) =>
          Left(DecodingFailure(s"UUID:$exception", c.history))
        case Success(value) =>
          value
            .map(address => Right(address))
            .getOrElse(Left(DecodingFailure("UUID", c.history)))
    }

    implicit def encodeEnum[A, C <: Coproduct](implicit
                                               gen: LabelledGeneric.Aux[A, C],
                                               rie: IsEnum[C]): Encoder[A] =
      Encoder[String].contramap[A](a => rie.to(gen.to(a)))

    implicit def decodeEnum[A, C <: Coproduct](implicit
                                               gen: LabelledGeneric.Aux[A, C],
                                               rie: IsEnum[C]): Decoder[A] =
      Decoder[String].emap { s =>
        rie.from(s).map(gen.from).toRight("enum")
      }

    implicit val configuration: Configuration =
      Configuration.default.withDiscriminator("type")
  }

  private object enum {

    import shapeless._
    import shapeless.labelled._

    trait IsEnum[C <: Coproduct] {
      def to(c: C): String

      def from(s: String): Option[C]
    }

    object IsEnum {
      implicit val cnilIsEnum: IsEnum[CNil] = new IsEnum[CNil] {
        def to(c: CNil): String = sys.error("Impossible")

        def from(s: String): Option[CNil] = None
      }

      implicit def cconsIsEnum[K <: Symbol, H <: Product, T <: Coproduct](
        implicit
        witK: Witness.Aux[K],
        witH: Witness.Aux[H],
        gen: Generic.Aux[H, HNil],
        tie: IsEnum[T]
      ): IsEnum[FieldType[K, H] :+: T] = new IsEnum[FieldType[K, H] :+: T] {

        def to(c: FieldType[K, H] :+: T): String = c match {
          case Inl(_) => witK.value.name
          case Inr(t) => tie.to(t)
        }

        def from(s: String): Option[FieldType[K, H] :+: T] =
          if (s == witK.value.name) Some(Inl(field[K](witH.value)))
          else tie.from(s).map(Inr(_))
      }
    }

  }

}
