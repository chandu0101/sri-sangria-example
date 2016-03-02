package sri.sangria.mongoserver.exceptions

import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import io.circe.generic.auto._
import sangria.marshalling.circe._
import sri.sangria.mongoserver.akkahttp2circe.AkkaHttpCirceSupport._

/**
 *  handle exceptions outside of sangria-graphql
 */
trait RouteExceptionHandler {

  implicit def exceptionHandler = ExceptionHandler {
    case e: RequestException => complete(BadRequest, getAppError(e))
    case e: QueryException => complete(BadRequest, getAppError(e))
    case e: DatabaseException => complete(InternalServerError, getAppError(e))
    case e: Exception => complete(InternalServerError, AppError(errorScope = "unknown", errorDetails = e.getMessage))
  }

}
