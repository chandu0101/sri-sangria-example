package sri.sangria.web.components

import sri.sangria.web.styles.Colors
import sri.universal.components.{View, _}
import sri.web.all._
import sri.web.styles.WebStyleSheet
import scala.scalajs.js.{UndefOr => U, undefined}

object Footer {

  val Component = () => View(style = styles.footer)(
    Text()("Built using Sri-Web")
  )

  object styles extends WebStyleSheet {

    val footer = style(alignItems.center,
      justifyContent.center,
      borderTopWidth := "1px",
      height := 70,
      borderTopColor := Colors.lightGrey)

  }

  def apply() = createStatelessFunctionElementNoProps(Component)
}
