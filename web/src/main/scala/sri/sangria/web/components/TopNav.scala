package sri.sangria.web.components

import sri.sangria.web.routes.AppRouter.{HomePage, TodosPage}
import sri.sangria.web.styles.Colors
import sri.universal.components._
import sri.web.all._
import sri.web.router
import sri.web.router.{WebRouterComponent, WebStaticPage}
import sri.web.styles.WebStyleSheet

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U}


object TopNav {

  @ScalaJSDefined
  class Component extends WebRouterComponent[Unit, Unit] {
    def render() = {
      View(style = styles.navMenu)(
        getStaticItem("Home", HomePage),
        getStaticItem("Todos", TodosPage)
      )
    }

    def getStaticItem(text: String, page: WebStaticPage, query: js.UndefOr[js.Object] = js.undefined, state: js.UndefOr[js.Object] = js.undefined) = {
      TouchableHighlight(style = styles.menuItem(page == currentRoute.page),
        onPress = () => navigateTo(page, query = query, state = state))(
          Text()(text)
        )
    }

  }

  @ScalaJSDefined
  class StaticQuery(val sorting: String, val option: js.UndefOr[String] = js.undefined) extends js.Object


  object styles extends WebStyleSheet {


    val navMenu = style(display.flex,
      flexDirection.row,
      alignItems.center,
      backgroundColor := Colors.gold,
      margin := 0,
      paddingLeft := 40)

    def menuItem(selected: Boolean) = {
      val bg = if (selected) Colors.darkGold else Colors.transparent
      val fw = if (selected) "500" else "normal"
      style(
        padding := 20,
        fontSize := "1.5em",
        cursor.pointer,
        color := "rgb(244, 233, 233)",
        backgroundColor := bg,
        fontWeight := fw,
        marginLeft := "10px",
        marginRight := "10px"
      )
    }

  }

  val ctor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  ctor.contextTypes = router.routerContextTypes


  def apply(ref: js.UndefOr[String] = "", key: js.Any = {}) = createElementNoProps(ctor)

}

