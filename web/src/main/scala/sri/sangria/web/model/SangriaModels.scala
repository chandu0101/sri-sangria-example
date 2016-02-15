package sri.sangria.web.model

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined


@ScalaJSDefined
trait SangriaViewer extends js.Object {

  val todos: SangriaConnection[TodoJS]

}

@ScalaJSDefined
trait SangriaConnection[T <: SangriaNode] extends js.Object {
  val edges: js.Array[SangriaEdge[T]]
  val count: Int
  val pageInfo: SangriaInfo
}

@ScalaJSDefined
trait SangriaInfo extends js.Object {
  val hasNextPage: Boolean
  val hasPreviousPage: Boolean
}

@ScalaJSDefined
trait SangriaEdge[T <: SangriaNode] extends js.Object {
  val node: T
}


@ScalaJSDefined
trait SangriaNode extends js.Object {
  val id: String
}

@ScalaJSDefined
trait TodoJS extends SangriaNode {
  val text: String
}