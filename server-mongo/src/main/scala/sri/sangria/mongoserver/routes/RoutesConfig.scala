package sri.sangria.mongoserver.routes

import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Credentials`, `Access-Control-Allow-Headers`, `Access-Control-Max-Age`}
import akka.http.scaladsl.server.Directives._
import io.circe.{Json}
import io.circe.parser._
import io.circe.generic.auto._
import sangria.marshalling.circe._
import sangria.execution.Executor
import sangria.introspection.introspectionQuery
import sangria.parser.QueryParser
import sri.sangria.mongoserver.akkahttp2circe.AkkaHttpCirceSupport._
import sri.sangria.mongoserver.exceptions
import sri.sangria.mongoserver.exceptions.{QueryException, RouteExceptionHandler, RequestException}
import sri.sangria.mongoserver.graphql.TodoRepo
import sri.sangria.mongoserver.graphql.schema.TodoSchema
import sri.sangria.mongoserver.services.{TodoService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

trait RoutesConfig extends CorsSupport with RouteExceptionHandler {

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
    exceptionHandler = exceptions.sangriaExceptionHandler,
    userContext = new TodoRepo)

  case class GraphQLInput(query: String, operation: Option[String], variables: Option[Json])

  val routes = cors {
    (post & path("graphql")) {
      entity(as[Json]) { requestJson =>

        val inputXor = requestJson.hcursor.as[GraphQLInput]

        if (inputXor.isLeft) throw new RequestException("Input request is not valid")

        val input = inputXor.getOrElse(null)

        val vars = input.variables.map(v => {
          v match {
            case o: Json if (o.isObject) => o
            case s: Json if (s.isString) => parse(s.asString.get).getOrElse(Json.obj())
            case _ => Json.obj()
          }
        }).getOrElse(Json.obj())

        QueryParser.parse(input.query) match {

          // query parsed successfully, time to execute it!
          case Success(queryAst) =>
            complete(executor.execute(queryAst,
              operationName = input.operation,
              variables = vars))

          // can't parse GraphQL query, return error
          case Failure(error) =>
            throw new QueryException(error.getMessage)
        }
      }
    } ~
      (path("introspect") & get) {
        // get schema.json for relay apps
        complete(executor.execute(introspectionQuery) map { introspectedSchema =>
          val schemaFilePath = "../data/schema.json"
          val outFile = new java.io.FileWriter(schemaFilePath)
          outFile.write(introspectedSchema.spaces2)
          outFile.close()
          "schema written successfully."
        })
      } ~
      (get & path("graphiql")) { // graphiql editor
        getFromResource("web/graphiql.html")
      }
  }

}
