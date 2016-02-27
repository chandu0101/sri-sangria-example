package sri.sangria.mongoserver.util

import io.circe.Decoder

object CirceUtils {

  def getCirceErrorMessage[T](in: Decoder.Result[T]) = in.swap.map(_.getMessage()).getOrElse("")
}
