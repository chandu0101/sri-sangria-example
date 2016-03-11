package sri.sangria.mobile.components

import sri.core._
import sri.sangria.mobile.model.TodoJS
import sri.sangria.mobile.styles.{Theme, Colors}
import sri.universal.ReactUniversal
import sri.universal.components._
import sri.mobile.all._
import sri.universal.styles.UniversalStyleSheet
import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U, Function1, undefined => undefined}


object TodoList {

  case class State(ds: ListViewDataSource[TodoJS, String] = createListViewDataSource((r1: TodoJS, r2: TodoJS) => r1 != r2))

  @ScalaJSDefined
  class Component extends ReactComponent[Props, State] {

    initialState(State())

    def render() = {
      val ds = state.ds.cloneWithRows(props.items)
      ListView(dataSource = ds,
        style = styles.container,
        renderRow = renderRow _,
        renderSeparator = renderSeparator _)()
    }

    def renderRow(row: TodoJS, sectionId: String, rowId: String, highlightRow: js.Function2[String, String, _]) = {
      View(style = styles.cell, key = rowId)(Text(style = Theme.bigText)(row.text))
    }

    def renderSeparator(sectionID: String, rowID: String, adjacentRowHighlighted: Boolean) = {
      View(style = styles.cellBorder, key = rowID)()
    }
  }

  object styles extends UniversalStyleSheet {

    val container = style(flexOne,alignSelf.stretch)

    val cell = style(height := 90,
      justifyContent.center,
      alignItems.center)

    val cellBorder = style(backgroundColor := "rgba(183, 84, 84, 0.4)",
      height := 1.0 / ReactUniversal.PixelRatio.get(),
      marginLeft := 4)

  }

  case class Props(items: js.Array[TodoJS])

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  def apply(items: js.Array[TodoJS], key: U[String] = js.undefined, ref: Function1[Component, _] = null) = createElement(ctor, Props(items), key = key, ref = ref)

}
