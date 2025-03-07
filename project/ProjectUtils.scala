import sbt.Keys._
import sbt._
import de.heikoseeberger.sbtheader.AutomateHeaderPlugin

import scala.sys.process._
import scala.util.Try

object ProjectUtils {
  type PE = Project => Project

  private val pathToSkipInNames = Set("libraries", "pocs", "component")

  private def generateName(path: String): String =
    path.split("/").filterNot(v => pathToSkipInNames.contains(v)).mkString("-")

  private def generateId(path: String): String =
    path.split("/").filterNot(v => pathToSkipInNames.contains(v)).flatMap(_.split("-")).reduce(_ + _.capitalize)

  def setupProject(path: String, publish: Boolean = true) = {
    val id = generateId(path)
    Project(id = id, file(path)).configure(setupDefaultProject(path, publish)).settings(Common.commonJvmSettings)
  }

  def setupDefaultProject(path: String, publish: Boolean = true)(
    project: Project
  ) = {
    val docName = path.split("/").map(_.capitalize).mkString(" ")
    val fullname = s"${Common.appName}-${generateName(path)}"
    project
      .enablePlugins(AutomateHeaderPlugin)
      .settings(
        description := s"${Common.appName.capitalize} $docName",
        moduleName := fullname,
        name := fullname
      )
      .settings(Common.commonGeneric)
  }

}
