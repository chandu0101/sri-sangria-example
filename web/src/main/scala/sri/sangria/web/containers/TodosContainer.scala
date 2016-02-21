package sri.sangria.web.containers

import sri.core._
import sri.relay.{RelayComponentProps, RelayComponent, Relay}
import sri.relay.container.RelayContainer.Fragments
import sri.relay.container.RelayContainer.Fragments
import sri.relay.container.{Fragments, RelayContainerSpec}
import sri.relay.query.RelayQL
import sri.sangria.web.components.TodosScreen
import sri.sangria.web.model.SangriaViewer
import sri.universal.components._
import sri.web.all._
import sri.relay.RelayElementFactory._
import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U, undefined => undefined}


object TodosContainer {


  @ScalaJSDefined
  class Component extends RelayComponent[Props, Unit] {
    def render() = {

      TodosScreen(props.viewer, props.relay)
    }
  }

  @ScalaJSDefined
  class Props(val viewer: SangriaViewer) extends RelayComponentProps

  val ctor = getRelayTypedConstructor(js.constructorOf[Component], classOf[Component])

  val container = Relay.createContainer(ctor, new RelayContainerSpec {
    override val fragments: Fragments = Fragments("viewer" -> (()
    => js.eval(RelayQL(
        """
          |fragment on User {
          |  id,
          |  todos(first : 10) {
          |    edges {
          |      node {
          |       id,
          |       text
          |      }
          |    }
          |  }
          |}
        """))))
  })

}
