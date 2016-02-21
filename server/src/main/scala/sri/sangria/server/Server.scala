package sri.sangria.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import sri.sangria.server.routes.RoutesConfig

object Server extends App with RoutesConfig {
  implicit val system = ActorSystem("sangria-server")
  implicit val materializer = ActorMaterializer()

  Http().bindAndHandle(routes, "0.0.0.0", sys.props.get("http.port").fold(8080)(_.toInt))
}
