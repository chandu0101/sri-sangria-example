import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._

object Dependencies {

  object Version {

    val scalaAsync = "0.9.2"

    val scalatest = "3.0.0-M6"

    val sangriaRelay = "0.5.2"

    val sangriaSprayJson = "0.1.0"

    val sangriaCirce = "0.3.1"

    val akkHttp = "2.0"

    val sri = "0.4.0"

    val reactiveMongo = "0.11.9"

    val circe = "0.3.0"
  }


  val scalatestJS = libraryDependencies += "org.scalatest" %%% "scalatest" % Version.scalatest % Test

  val scalaAsync = libraryDependencies += "org.scala-lang.modules" %% "scala-async" % Version.scalaAsync

  val sriUniversal = libraryDependencies += "com.github.chandu0101.sri" %%% "universal" % Version.sri

  val sriMobile = libraryDependencies += "com.github.chandu0101.sri" %%% "mobile" % Version.sri

  val sriWeb = libraryDependencies += "com.github.chandu0101.sri" %%% "web" % Version.sri

  val sriRelay = libraryDependencies += "com.github.chandu0101.sri" %%% "relay" % Version.sri

  val sangriaRelay = libraryDependencies += "org.sangria-graphql" %% "sangria-relay" % Version.sangriaRelay

  val sangriaSprayJson = libraryDependencies += "org.sangria-graphql" %% "sangria-spray-json" % Version.sangriaSprayJson

  val sangriaCirceJson = libraryDependencies += "org.sangria-graphql" %% "sangria-circe" % Version.sangriaCirce

  val akkaHttp = libraryDependencies += "com.typesafe.akka" %% "akka-http-experimental" % Version.akkHttp

  val akkaHttpSprayJson = libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json-experimental" % Version.akkHttp

  val circeCore = libraryDependencies += "io.circe" %%% "circe-core" % Version.circe

  val circeGeneric = libraryDependencies += "io.circe" %%% "circe-generic" % Version.circe

  val circeParseJS = libraryDependencies += "io.circe" %%% "circe-parser" % Version.circe

  val circeParseJVM = libraryDependencies += "io.circe" %% "circe-parser" % Version.circe

  val reactiveMongo = libraryDependencies += "org.reactivemongo" %% "reactivemongo" % Version.reactiveMongo


  val universalModuleDeps = Seq(
    sriUniversal,
    scalaAsync
  )

  val serverModuleDeps = Seq(sangriaRelay,
    sangriaCirceJson,
    circeParseJVM,
    akkaHttp)

  val mongoServerModuleDeps = Seq(sangriaRelay,
    sangriaCirceJson,
    circeParseJVM,
    reactiveMongo,
    akkaHttp)

  val webModuleDeps = Seq(
    sriWeb,
    sriRelay
  )

  val mobileModuleDeps = Seq(
    sriMobile,
    sriRelay
  )

}