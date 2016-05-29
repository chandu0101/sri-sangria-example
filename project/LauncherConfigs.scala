import org.scalajs.sbtplugin.ScalaJSPlugin.AutoImport._
import sbt.Keys._
import sbt._

object LauncherConfigs {

  /** ================ React_native task   ================ */

  val fastOptMobile = Def.taskKey[File]("Generate mobile output file for fastOptJS")

  lazy val mobileLauncherFast =
    Seq(
      artifactPath in Compile in fastOptMobile :=
        baseDirectory.value / "index.ios.js",
      fastOptMobile in Compile := {
        val outFile = (artifactPath in Compile in fastOptMobile).value

        val loaderFile = (resourceDirectory in Compile).value / "loader.js"

        IO.copyFile(loaderFile, outFile)

        val fullOutputCode = IO.read((fastOptJS in Compile).value.data)

        val outString = processRequireFunctionsInFastOpt(fullOutputCode)

        IO.write(baseDirectory.value / "scalajs-output.js", outString)

        val launcher = (scalaJSLauncher in Compile).value.data.content
        IO.append(outFile, launcher)

        IO.copyFile(outFile, baseDirectory.value / "index.android.js")
        outFile
      }
    )

  val fullOptMobile = Def.taskKey[File]("Generate the file given to react native")

  lazy val mobilelauncherFull =
    Seq(
      artifactPath in Compile in fullOptMobile :=
        baseDirectory.value / "index.ios.js",
      fullOptMobile in Compile := {
        val outFile = (artifactPath in Compile in fullOptMobile).value

        val loaderFile = (resourceDirectory in Compile).value / "loader.js"

        IO.copyFile(loaderFile, outFile)

        val fullOutputCode = IO.read((fullOptJS in Compile).value.data)

        val outString = processRequireFunctions(fullOutputCode)

        IO.write(baseDirectory.value / "scalajs-output.js", outString)

        val launcher = (scalaJSLauncher in Compile).value.data.content
        IO.append(outFile, launcher)

        IO.copyFile(outFile, baseDirectory.value / "index.android.js")
        outFile
      }
    )

  /**
   * react-native prod bundler needs require function without name spaces
   * @param text
   * @return
   */
  def processRequireFunctions(text: String): String = {
    val SJS_NAME_SPACE = "exportsNamespace:"
    val i = text.indexOf(SJS_NAME_SPACE) + SJS_NAME_SPACE.length
    val j = text.substring(i).indexOf(";") + i // TODO look for non valid identifier ![_$0-9a-zA-Z]
    val nameSpace = text.substring(i,j)
    text.replaceAll(s"$nameSpace.require\\(", "require\\(")
  }

  /**
   * react-native prod bundler needs require function without name spaces
   * @param text
   * @return
   */
  def processRequireFunctionsInFastOpt(text: String): String = {
    text.replaceAll("\\$g.require\\(", "require\\(")
  }

  /** *  Web Tasks             ***/

  val webAssets = "web/assets"

  lazy val webLauncher = Seq(crossTarget in(Compile, fullOptJS) := file(webAssets),
    crossTarget in(Compile, fastOptJS) := file(webAssets),
    crossTarget in(Compile, packageScalaJSLauncher) := file(webAssets),
    artifactPath in(Compile, fastOptJS) := ((crossTarget in(Compile, fastOptJS)).value /
      ((moduleName in fastOptJS).value + "-opt.js"))
  )


}