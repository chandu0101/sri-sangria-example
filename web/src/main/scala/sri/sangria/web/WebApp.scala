package sri.sangria.web

import org.scalajs.dom
import sri.relay.Relay
import sri.relay.network.DefaultNetworkLayer
import sri.relay.query.RelayQL
import sri.sangria.web.mutations.AddTodoMutation
import sri.sangria.web.routes.AppRouter
import sri.web.ReactDOM

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g, literal => json}
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

object WebApp extends JSApp {


  @JSExport
  override def main(): Unit = {
    Relay.injectNetworkLayer(new DefaultNetworkLayer("http://localhost:8080/graphql"))

    ReactDOM.render(AppRouter.router, dom.document.getElementById("app"))
  }

}
