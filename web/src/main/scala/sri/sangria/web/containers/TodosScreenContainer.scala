package sri.sangria.web.containers

import sri.relay.RelayElementFactory._
import sri.relay.container.RelayContainer.Fragments
import sri.relay.container.{Fragments, RelayContainerSpec}
import sri.relay.query.RelayQL
import sri.relay.{Relay, RelayComponent, RelayComponentProps}
import sri.sangria.web.components.{TodoList, TodoInput}
import sri.sangria.web.model.SangriaViewer
import sri.sangria.web.mutations.AddTodoMutation
import sri.universal.components.View
import sri.web.styles.WebStyleSheet

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.annotation.ScalaJSDefined


object TodosScreenContainer {


  @ScalaJSDefined
  class Component extends RelayComponent[Props, Unit] {
    def render() = {
      View(style = styles.container)(
        View(style = styles.todos)(
          TodoInput(onSave = onAddClick _),
          TodoList(props.viewer.todos.edges.map(_.node))
        )
      )
    }

    def onAddClick(text: String) = {
      Relay.Store.commitUpdate(new AddTodoMutation(json(viewer = props.viewer, text = text)))
    }

  }

  object styles extends WebStyleSheet {

    val container = style(flex := 1, padding := 40, alignItems.center)

    val todos = style(flex := 1,
      padding := 30,
      width := 600,
      boxShadow := "0 2px 4px grey")
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
