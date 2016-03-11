package sri.sangria.mobile.components

import sri.sangria.mobile.routes.AppRouter.TodosPage
import sri.universal.components._
import sri.universal.router
import sri.universal.router.UniversalRouterComponent
import sri.universal.styles.UniversalStyleSheet
import sri.mobile.all._
import scala.scalajs.js.Dynamic.{literal => json}

import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

object HomeScreen {

  @ScalaJSDefined
  class Component extends UniversalRouterComponent[Unit, Unit] {
    def render() = {
      View(style = styles.container)(
        Button("Todos",openTodos _,styles.button)
      )
    }

    def openTodos() = {
      navigateTo(TodosPage)
    }
  }

  object styles extends UniversalStyleSheet {

    val container = style(flexOne,
       marginTop := 20,
      marginLeft := 20)

    val image = style(width := 256, height := 256, margin := 20)

    val text = style(fontWeight._500,
      fontSize := 18,
      color := "#E0DFDF")

    val button = style(width := 150,
      height := 150,
     backgroundColor := "#E84254",
      shadowColor := "black",
      shadowOpacity := 0.2,
      shadowRadius := 2,
      justifyContent.center,
      alignItems.center,

      shadowOffset := json(height = 1, width = 0))
//    val buttonCommon = style()

  }

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  ctor.contextTypes = router.routerContextTypes

  def apply(key: js.UndefOr[String] = js.undefined, ref: js.Function1[Component, _] = null) = createElementNoProps(ctor, key = key, ref = ref)
}