package sri.sangria.web.queries

import sri.relay.container.RelayContainer.RootQueries
import sri.relay.container.RootQueries
import sri.relay.query.RelayQL
import sri.relay.route.RelayQueryConfig
import scala.scalajs.js
import scalajs.js.Dynamic.{literal => json}

import scala.scalajs.js.Object

object ViewerQuery {

  def apply() = new RelayQueryConfig {
    override val name: String = "ViewerQuery"
    override val queries: RootQueries = RootQueries("viewer" -> (() => js.eval(RelayQL( """query { viewer}"""))))
    override val params: Object = json()
  }

}