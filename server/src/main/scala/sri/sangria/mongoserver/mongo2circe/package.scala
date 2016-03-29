package sri.sangria.mongoserver

import java.time._

import cats.data.Xor
import io.circe.{DecodingFailure, Encoder, Decoder, Json}
import reactivemongo.bson.BSONObjectID

import scala.util.Try

/**
 *  Circe Json to BSON conversion
 */
package object mongo2circe {

  val $OID = "$oid"

  val $DATE_TIME = "$datetime"

  implicit final def decodeBSONObjectID: Decoder[BSONObjectID] =
    Decoder.instance[BSONObjectID](c => {
      Try(Xor.Right(BSONObjectID(c.focus.asObject.get($OID).get.asString.get))).toOption.getOrElse(Xor.Left(DecodingFailure("failed in decoding BSONObjectId(oid)", c.history)))
    })

  implicit final def encodeBSONObjectID: Encoder[BSONObjectID] =
    Encoder.instance[BSONObjectID](bid => Json.obj($OID -> Json.string(bid.stringify)))

  implicit final def decodeLocalDateTime: Decoder[LocalDateTime] =
    Decoder.instance[LocalDateTime](c => {
      Try(Xor.Right(LocalDateTime.ofInstant(Instant.ofEpochMilli(c.focus.asObject.get($DATE_TIME).get.asNumber.get.toLong.get), ZoneOffset.ofHoursMinutes(5, 30)))).toOption.getOrElse(Xor.Left(DecodingFailure("failed in decoding LocalDateTime", c.history)))
    })

  implicit final def encodeLocalDateTime: Encoder[LocalDateTime] =
    Encoder.instance[LocalDateTime](dt => Json.obj($DATE_TIME -> Json.long(dt.toInstant(ZoneOffset.ofHoursMinutes(5, 30)).toEpochMilli)))

}
