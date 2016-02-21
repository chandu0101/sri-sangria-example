package sri.sangria.server

import cats.data.Xor
import io.circe.{DecodingFailure, Decoder, Error, Json}

package object util {

  def decodeJson[T](in: Json)(implicit decoder: Decoder[T]): Xor[DecodingFailure, T] = decoder.decodeJson(in)

}
