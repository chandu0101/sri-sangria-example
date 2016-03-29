package sri.sangria.mongoserver.akkahttp2circe

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.{HttpCharsets, MediaTypes}
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import cats.data.Xor
import io.circe.{Decoder, Encoder, Json, Printer, jawn}

/**
 * https://github.com/hseeberger/akka-http-json/tree/master/akka-http-circe
 *
 * Automatic to and from JSON marshalling/unmarshalling using an in-scope *Circe* protocol.
 *
 * To use automatic codec derivation, user need to import `circe.generic.auto._`.
 */
object AkkaHttpCirceSupport extends AkkaHttpCirceSupport

/**
 * JSON marshalling/unmarshalling using an in-scope *Circe* protocol.
 *
 * To use automatic codec derivation, user need to import `circe.generic.auto._`
 */
trait AkkaHttpCirceSupport {

  implicit def circeUnmarshallerConverter[A](decoder: Decoder[A]): FromEntityUnmarshaller[A] =
    circeUnmarshaller(decoder)

  /**
   * HTTP entity => `A`
   *
   * @param decoder decoder for `A`, probably created by `circe.generic`
   * @tparam A type to decode
   * @return unmarshaller for `A`
   */
  implicit def circeUnmarshaller[A](implicit decoder: Decoder[A]): FromEntityUnmarshaller[A] =
    circeJsonUnmarshaller.map { json =>
      decoder.decodeJson(json) match {
        case Xor.Right(entity) => entity
        case Xor.Left(e) => throw e
      }
    }

  /**
   * HTTP entity => JSON
   *
   * @return unmarshaller for Circe Json
   */
  implicit def circeJsonUnmarshaller: FromEntityUnmarshaller[Json] =
    Unmarshaller
      .byteStringUnmarshaller
      .forContentTypes(MediaTypes.`application/json`)
      .mapWithCharset { (data, charset) =>
      val input =
        if (charset == HttpCharsets.`UTF-8`) data.utf8String
        else data.decodeString(charset.nioCharset.name)
      jawn.parse(input) match {
        case Xor.Right(json) => json
        case Xor.Left(e) => throw e
      }
    }

  implicit def circeToEntityMarshallerConverter[A](encoder: Encoder[A])(implicit printer: Json => String = Printer.noSpaces.pretty): ToEntityMarshaller[A] =
    circeToEntityMarshaller(encoder)

  /**
   * `A` => HTTP entity
   *
   * @param encoder encoder for `A`, probably created by `circe.generic`
   * @param printer pretty printer function
   * @tparam A type to encode
   * @return marshaller for any `A` value
   */
  implicit def circeToEntityMarshaller[A](implicit encoder: Encoder[A], printer: Json => String = Printer.noSpaces.pretty): ToEntityMarshaller[A] =
    circeJsonMarshaller.compose(encoder.apply)

  /**
   * JSON => HTTP entity
   *
   * @param printer pretty printer function
   * @return marshaller for any Json value
   */
  implicit def circeJsonMarshaller(implicit printer: Json => String = Printer.noSpaces.pretty): ToEntityMarshaller[Json] =
    Marshaller.StringMarshaller.wrap(MediaTypes.`application/json`)(printer)
}