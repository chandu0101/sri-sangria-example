package sri.sangria.mobile.components

import sri.universal.components.{Text, View}
import sri.universal.styles.UniversalStyleSheet
import sri.mobile.all._

import scala.scalajs.js.{UndefOr => U}


object LoadingIndicator {

  val Component = () => View(style = styles.container)(
    Text(style = styles.text)("Loading ..")
  )

  object styles extends UniversalStyleSheet {

    val container = style(flexOne,
      justifyContent.center,
      alignItems.center)

    val text = style(fontWeight.bold,
      color := "red")

  }

  def apply() = createStatelessFunctionElementNoProps(Component)
}