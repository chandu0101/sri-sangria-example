package sri.sangria.backend

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import sri.sangria.backend.routes.RoutesConfig

object Server extends App {
  implicit val system = ActorSystem("sangria-server")
  implicit val materializer = ActorMaterializer()

  Http().bindAndHandle(RoutesConfig.routes, "0.0.0.0", sys.props.get("http.port").fold(8080)(_.toInt))
}
