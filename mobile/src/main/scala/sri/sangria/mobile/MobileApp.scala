package sri.sangria.mobile

import sri.mobile.ReactNative
import sri.mobile.all._
import sri.relay.Relay
import sri.relay.network.DefaultNetworkLayer
import sri.sangria.mobile.routes.AppRouter

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.{JSApp}


object MobileApp extends JSApp {


  @JSExport
  override def main(): Unit = {
    Relay.injectNetworkLayer(new DefaultNetworkLayer("http://localhost:8080/graphql"))

    val root = createMobileRoot(
      AppRouter.router
    )

    ReactNative.AppRegistry.registerComponent("SriSangriaExampleMobile", () => root)
  }

}
