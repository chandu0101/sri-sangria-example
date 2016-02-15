package sri.sangria.web.components

import sri.core._
import sri.sangria.web.styles.Theme
import sri.universal.components._
import sri.universal.styles.UniversalStyleSheet
import sri.web.all._

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

object HomeScreen {

  @ScalaJSDefined
  class Component extends ReactComponent[Unit, Unit] {
    def render() = {
      View(style = Theme.flexOneAndCenter)(
        Text(style = Theme.bigText)("Welcome to Sri Sangria Example :) ")
      )
    }
  }

  object styles extends UniversalStyleSheet {

    val container = style(flexOne,
      backgroundColor := "rgb(156, 11, 144)",
      justifyContent.center,
      alignItems.center)

    val image = style(width := 256, height := 256, margin := 20)

    val text = style(fontWeight._500,
      fontSize := 18,
      color := "#E0DFDF")

    val button = style(width := 100,
      height := 100,
      borderColor := "red",
      borderWidth := 2)

  }

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  def apply(key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElementNoProps(ctor, key = key, ref = ref)
}