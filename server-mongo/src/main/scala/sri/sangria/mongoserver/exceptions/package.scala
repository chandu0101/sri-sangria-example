package sri.sangria.mongoserver

import sangria.execution.HandledException
import sangria.marshalling.ResultMarshaller
import io.circe.syntax._
import io.circe.generic.auto._

package object exceptions {

  val sangriaExceptionHandler: PartialFunction[(ResultMarshaller, Throwable), HandledException] = {
    case (m, e: Exception) => e match {
      case qex: QueryException => HandledException(getErrorString(qex))
      case dbex: DatabaseException => HandledException(getErrorString(dbex))
      case mex: MutationException => HandledException(getErrorString(mex))
      case _ => HandledException(e.getMessage)
    }
  }

  def getErrorJson(ex: BaseException) = AppError(errorScope = ex.name, errorDetails = ex.msg).asJson

  def getErrorString(ex: BaseException) = getErrorJson(ex).noSpaces
}
