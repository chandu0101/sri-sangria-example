package sri.sangria.mobile.components

import sri.mobile.all._
import sri.sangria.mobile.styles.Theme
import sri.universal.components.{Text, TouchableHighlight}

import scala.scalajs.js

object Button {
  val Component = (props: Props) => {
    TouchableHighlight(style = props.style, onPress = props.onPress)(
      Text(style = Theme.whiteText500)(props.label)
    )
  }

  case class Props(label: String,onPress: () => _,style : js.Any)

  def apply(label: String,onPress: () => _,style : js.Any) = createStatelessFunctionElement(Component, Props(label,onPress,style))
}

