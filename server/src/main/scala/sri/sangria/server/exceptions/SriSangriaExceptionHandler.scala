package sri.sangria.server.exceptions

import akka.http.scaladsl.server.ExceptionHandler
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import io.circe.Json
import io.circe.syntax._
import io.circe.generic.auto._
import sangria.marshalling.circe._
import sri.sangria.server.akkahttp2circe.AkkaHttpCirceSupport._


trait SriSangriaExceptionHandler {

  implicit def exceptionHandler = ExceptionHandler {
    case e: RequestException => complete(BadRequest, SriSangriaError(errorScope = "request", message = e.getMessage).asJson)
    case e: QueryException => complete(BadRequest, SriSangriaError(errorScope = "query", message = e.getMessage).asJson)
    case e: Exception => complete(InternalServerError, SriSangriaError(errorScope = "unknown", message = e.getMessage).asJson)
  }

}
