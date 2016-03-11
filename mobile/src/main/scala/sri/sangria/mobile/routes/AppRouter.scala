package sri.sangria.mobile.routes

import sri.core.ReactElement
import sri.relay.container.RelayRootContainer
import sri.sangria.mobile.components.{HomeScreen, LoadingIndicator}
import sri.sangria.mobile.containers.TodosScreenContainer
import sri.sangria.mobile.queries.ViewerQuery
import sri.universal.components.DefaultNavigationBar.Style
import sri.universal.components.{DefaultNavigationBar, View}
import sri.universal.router.{NavigatorRoute, StaticPage, UniversalRouter, UniversalRouterConfig}
import sri.universal.styles.UniversalStyleSheet

object AppRouter {

  object HomePage extends StaticPage

  object TodosPage extends StaticPage

  object Config extends UniversalRouterConfig {


    override val initialRoute: (StaticPage, NavigatorRoute) = defineInitialRoute(HomePage, "SriSangria", HomeScreen())

    staticRoute(TodosPage, "Todos", RelayRootContainer(Component = TodosScreenContainer.container, query = ViewerQuery(), renderLoading = () => LoadingIndicator()))

    override val notFound: (StaticPage, NavigatorRoute) = initialRoute

    override def renderScene(route: NavigatorRoute): ReactElement = {
      View(style = UniversalStyleSheet.wholeContainer)(
        DefaultNavigationBar(CustomNavigationBarTheme),
        super.renderScene(route)
      )
    }
  }


  val router = UniversalRouter(Config, style = styles.rootStyle)

}


object styles extends UniversalStyleSheet {

  val rootStyle = style(backgroundColor := "#f2606f")


}

object CustomNavigationBarTheme extends Style {

  override val navBar = styleE(super.navBar)(backgroundColor := "#E84254", borderBottomColor := "#D66767")

  override val navBarTitleText = styleE(super.navBarTitleText)(color := "white")

  override val navBarLeftButton = super.navBarLeftButton

  override val navBarButtonText = styleE(super.navBarButtonText)(color := "white")

}