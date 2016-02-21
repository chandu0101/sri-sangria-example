package sri.sangria.web.components

import sri.core._
import sri.sangria.web.styles.Colors
import sri.universal.components.{Text, TouchableHighlight, _}
import sri.web.all._
import sri.web.styles.WebStyleSheet

import scala.scalajs.js
import scala.scalajs.js.annotation.{ExposedJSMember, JSName, ScalaJSDefined}
import scala.scalajs.js.{UndefOr => U, undefined}

object TodoInput {

  object Button {
    val Component = (props: Props) => {
      TouchableHighlight(style = styles.button, onPress = props.onPress)(
        Text()("Add")
      )
    }

    case class Props(onPress: () => _)

    def apply(onPress: () => _) = createStatelessFunctionElement(Component, Props(onPress))
  }

  case class State(value: String = "")

  @ScalaJSDefined
  class Component extends ReactComponent[Props, State] {

    initialState(State())

    def render() = {
      View(style = styles.container)(
        TextInput(style = styles.input,
          value = state.value,
          placeholder = " Enter Todo Text",
          onChangeText = onTextChange _)(),
        Button(onPress = onAdd _)
      )
    }


    @JSName("sComponentWillReceiveProps")
    @ExposedJSMember
    override def componentWillReceiveProps(nextProps: => Props): Unit = {
      setState(state.copy(value = ""))
    }

    def onTextChange(str: String) = {
      setState(state.copy(value = str))
    }

    def onAdd() = {
      println(s"on add locked")
      props.onSave(state.value)
    }
  }

  case class Props(onSave: String => _)

  object styles extends WebStyleSheet {

    val container = style(flexDirection.row)

    val input = style(
      borderBottomColor := "grey",
      borderBottomWidth := "1px",
      flex := 1,
      marginRight := 5,
      padding := "3px")

    val button = style(padding := 10,
      borderWidth := "1px",
      cursor.pointer,
      borderColor := Colors.lightGrey
    )

  }

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  def apply(onSave: String => _, key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElement(ctor, Props(onSave), key = key, ref = ref)

}
