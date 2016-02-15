package sri.sangria.web.components

import sri.core._
import sri.relay.Relay
import sri.relay.tools.RelayProp
import sri.sangria.web.model.SangriaViewer
import sri.sangria.web.mutations.AddTodoMutation
import sri.universal.components._
import sri.web.all._
import sri.web.styles.WebStyleSheet
import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U, undefined => undefined}
import scala.scalajs.js.Dynamic.{literal => json}


object TodosScreen {

  @ScalaJSDefined
  class Component extends ReactComponent[Props, Unit] {

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

  case class Props(viewer: SangriaViewer, relay: RelayProp)

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  def apply(viewr: SangriaViewer, relay: RelayProp, key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElement(ctor, Props(viewr, relay), key = key, ref = ref)

}
