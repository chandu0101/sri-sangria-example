package sri.sangria.web.routes

import sri.core.ReactElement
import sri.relay.container.RelayRootContainer
import sri.relay.tools.ComponentFetchState
import sri.sangria.web.components.{LoadingIndicator, Footer, TopNav, HomeScreen}
import sri.sangria.web.containers.TodosContainer
import sri.sangria.web.model.SangriaViewer
import sri.sangria.web.queries.ViewerQuery
import sri.universal.components.View
import sri.web.router._
import sri.web.styles.WebStyleSheet

import scala.scalajs.js

object AppRouter {

  object HomePage extends WebStaticPage

  object TodosPage extends WebStaticPage

  object config extends WebRouterConfig {
    override val history: History = HistoryFactory.browserHistory()

    override val initialRoute: (WebStaticPage, WebRoute) = defineInitialRoute(HomePage, HomeScreen())

    staticRoute(TodosPage, "todos", RelayRootContainer(Component = TodosContainer.container, query = ViewerQuery(), renderLoading = () => LoadingIndicator()))

    override def renderScene(route: WebRoute): ReactElement = {
      View(style = WebStyleSheet.wholeContainer)(
        TopNav(),
        super.renderScene(route),
        Footer()
      )
    }

    override val notFound = WebRouteNotFound(HomePage)
  }

  val router = WebRouter(config)
}
