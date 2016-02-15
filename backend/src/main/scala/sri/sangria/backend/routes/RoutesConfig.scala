package sri.sangria.backend.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Credentials`, `Access-Control-Allow-Headers`, `Access-Control-Max-Age`}
import akka.http.scaladsl.server.Directives._
import sangria.execution.Executor
import sangria.introspection.introspectionQuery
import sangria.marshalling.sprayJson._
import sangria.parser.QueryParser
import spray.json.{JsObject, JsString, JsValue, _}
import sri.sangria.backend.schema.TodoSchema
import sri.sangria.backend.services.{InMemoryTodoRepo, TodoRepo}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Failure, Success}

object RoutesConfig extends CorsSupport {

  override val corsAllowOrigins: List[String] = List("*")

  override val corsAllowedHeaders: List[String] = List("Origin", "X-Requested-With", "Content-Type", "Accept", "Accept-Encoding", "Accept-Language", "Host", "Referer", "User-Agent")

  override val corsAllowCredentials: Boolean = true

  override val optionsCorsHeaders: List[HttpHeader] = List[HttpHeader](
    `Access-Control-Allow-Headers`(corsAllowedHeaders.mkString(", ")),
    `Access-Control-Max-Age`(60 * 60 * 24 * 20), // cache pre-flight response for 20 days
    `Access-Control-Allow-Credentials`(corsAllowCredentials)
  )

  val executor = Executor(
    schema = TodoSchema.schema,
    userContext = new InMemoryTodoRepo)

  val routes = cors {
    (post & path("graphql")) {
      entity(as[JsValue]) { requestJson =>
        val JsObject(fields) = requestJson

        val JsString(query) = fields("query")

        val operation = fields.get("operation") collect {
          case JsString(op) => op
        }

        val vars = fields.get("variables") match {
          case Some(obj: JsObject) => obj
          case Some(JsString(s)) => s.parseJson
          case _ => JsObject.empty
        }

        QueryParser.parse(query) match {

          // query parsed successfully, time to execute it!
          case Success(queryAst) =>
            complete(executor.execute(queryAst,
              operationName = operation,
              variables = vars))

          // can't parse GraphQL query, return error
          case Failure(error) =>
            complete(BadRequest, JsObject("error" -> JsString(error.getMessage)))
        }
      }
    } ~
      (path("introspect") & get) {
        complete(executor.execute(introspectionQuery) map { introspectedSchema =>
          val schemaFilePath = "../data/schema.json"
          val outFile = new java.io.FileWriter(schemaFilePath)
          outFile.write(introspectedSchema.prettyPrint)
          outFile.close()

          "schema written successfully."
        })
      }
  }

}
