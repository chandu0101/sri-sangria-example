import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt._

object Dependencies {

  val scalaAsyncVersion = "0.9.2"

  val upickleVersion = "0.3.1"

  val scalatestVersion = "3.0.0-M6"

  val sangriaRelayVersion = "0.5.1"

  val sangriaSprayJsonVersion = "0.1.0"

  val akkHttpVersion = "2.0"

  val sriVersion = "0.4.0-SNAPSHOT"

  val reactiveMongoVersion = "0.11.9"


  val scalatestJS = libraryDependencies += "org.scalatest" %%% "scalatest" % scalatestVersion % Test

  val scalaAsync = libraryDependencies += "org.scala-lang.modules" %% "scala-async" % scalaAsyncVersion

  val scalaJSUpickle = libraryDependencies += "com.lihaoyi" %%% "upickle" % upickleVersion

  val sriUniversal = libraryDependencies += "com.github.chandu0101.sri" %%% "universal" % sriVersion

  val sriMobile = libraryDependencies += "com.github.chandu0101.sri" %%% "mobile" % sriVersion

  val sriWeb = libraryDependencies += "com.github.chandu0101.sri" %%% "web" % sriVersion

  val sriRelay = libraryDependencies += "com.github.chandu0101.sri" %%% "relay" % sriVersion

  val sangriaRelay = libraryDependencies += "org.sangria-graphql" %% "sangria-relay" % sangriaRelayVersion

  val sangriaSprayJson = libraryDependencies += "org.sangria-graphql" %% "sangria-spray-json" % sangriaSprayJsonVersion

  val akkaHttp = libraryDependencies += "com.typesafe.akka" %% "akka-http-experimental" % akkHttpVersion

  val akkaHttpSprayJson = libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkHttpVersion

  val reactiveMongo = libraryDependencies += "org.reactivemongo" %% "reactivemongo" % reactiveMongoVersion


  val universalModuleDeps = Seq(
    sriUniversal,
    scalaAsync
  )

  val backendModuleDeps = Seq(sangriaRelay,
    sangriaSprayJson,
    akkaHttp,
    akkaHttpSprayJson)

  val webModuleDeps = Seq(
    sriWeb,
    sriRelay
  )

  val mobileModuleDeps = Seq(
    sriMobile
  )

}