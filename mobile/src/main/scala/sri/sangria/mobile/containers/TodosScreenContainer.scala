package sri.sangria.mobile.containers

import sri.relay.RelayElementFactory._
import sri.relay.container.RelayContainer.Fragments
import sri.relay.container.{Fragments, RelayContainerSpec}
import sri.relay.query.RelayQL
import sri.relay.{Relay, RelayComponent, RelayComponentProps}
import sri.sangria.mobile.components.{TodoList, TodoInput}
import sri.sangria.mobile.model.SangriaViewer
import sri.sangria.mobile.mutations.AddTodoMutation
import sri.universal.components.View
import sri.universal.styles.UniversalStyleSheet

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.annotation.ScalaJSDefined


object TodosScreenContainer {


  @ScalaJSDefined
  class Component extends RelayComponent[Props, Unit] {
    def render() = {
      View(style = UniversalStyleSheet.wholeContainer)(
          TodoInput(onSave = onAddClick _),
          TodoList(props.viewer.todos.edges.map(_.node))
      )
    }

    def onAddClick(text: String) = {
      Relay.Store.commitUpdate(new AddTodoMutation(json(viewer = props.viewer, text = text)))
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
          |  todos(first : 100) {
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
