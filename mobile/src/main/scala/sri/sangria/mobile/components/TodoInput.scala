package sri.sangria.mobile.components

import sri.core._
import sri.sangria.mobile.styles.Colors
import sri.universal.components.{Text, TouchableHighlight, _}
import sri.mobile.all._
import sri.universal.styles.UniversalStyleSheet

import scala.scalajs.js
import scala.scalajs.js.annotation.{ExposedJSMember, JSName, ScalaJSDefined}

object TodoInput {


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
        Button("Add", onPress = onAdd _, styles.button)
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

  object styles extends UniversalStyleSheet {

    val container = style(flexDirection.row,marginTop := 5)

    val input = style(
      flex := 1,
      height := 40,
      marginLeft := 3,
      marginRight := 5)

    val button = style(padding := 10,
      borderWidth := 1,
      borderColor := Colors.lightGrey,
      marginRight := 5
    )

  }

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  def apply(onSave: String => _, key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElement(ctor, Props(onSave), key = key, ref = ref)

}
