package sri.sangria.mongoserver.util

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object FutureUtils {

  def awaitFuture[T](f: Future[T]): T = Await.result(f, 30.seconds)

}
