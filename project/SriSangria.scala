import sbt._
import Keys._

import org.scalajs.sbtplugin.ScalaJSPlugin
import ScalaJSPlugin._
import autoImport._

object SriSangria extends Build {

  import Dependencies._
  import LauncherConfigs._

  val Scala211 = "2.11.7"

  lazy val commonSettings =
    Seq(
      organization := "sri.sangria",
      version := "0.1.0-SNAPSHOT",
      homepage := Some(url("https://github.com/chandu0101/sri-sangria-example")),
      scalaVersion := Scala211,
      resolvers ++= Seq(
        "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
        "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
      ),
      scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature",
        "-language:postfixOps", "-language:implicitConversions",
        "-language:higherKinds", "-language:existentials"))


  def DefProject(name: String, id: String = "") = {
    Project(if (id.isEmpty) name else id, file(name))
      .settings(commonSettings: _*)
  }

  def DefJSProject(name: String, id: String = "") = {
    Project(if (id.isEmpty) name else id, file(name))
      .settings(commonSettings: _*)
      .enablePlugins(ScalaJSPlugin)
  }

  def addCommandAliases(m: (String, String)*) = {
    val s = m.map(p => addCommandAlias(p._1, p._2)).reduce(_ ++ _)
    (_: Project).settings(s: _*)
  }

  /** ===================  Test frameworks settings   */

  val scalatestJSSettings = Seq(scalatestJS,
    scalaJSStage in Global := FastOptStage,
    jsDependencies += RuntimeDOM
    //    jsEnv in Test := new NodeJSEnv()
  )

  lazy val root =
    Project("root", file("."))
      .aggregate(web,mobile, shared.js, shared.jvm, server, serverMongo)

  // Scala-Js web frontend
  lazy val web =
    DefJSProject("web", "web")
      .settings(webModuleDeps)
      .settings(webLauncher)
      .dependsOn(sharedJs)

  // Scala-Js mobile client
  lazy val mobile =
    DefJSProject("mobile", "mobile")
      .settings(mobileModuleDeps)
      .settings(mobileLauncherFast)
      .settings(mobileLauncherFull)
      .dependsOn(sharedJs)

  // Akka Http based backend
  lazy val server =
    DefProject("server", "server")
      .settings(serverModuleDeps)
      .dependsOn(sharedJvm)

  // Akka Http based backend , mongo as DB
  lazy val serverMongo =
    DefProject("server-mongo", "server-mongo")
      .settings(mongoServerModuleDeps)
      .dependsOn(sharedJvm)


  lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
    settings(
      scalaVersion := Scala211
    )

  lazy val sharedJvm = shared.jvm
  lazy val sharedJs = shared.js

}
