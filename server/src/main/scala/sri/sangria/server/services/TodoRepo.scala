package sri.sangria.server.services

import sangria.schema.Args
import sri.sangria.server.models.{Todo, User}

import scala.concurrent.Future

trait TodoRepo {

  def addTodo(text: String): Todo

  def getTodos(ids: Seq[String],args : Args) : Future[Seq[Option[Todo]]]

  def getTodo(id: String): Option[Todo]

  def getUser(): Option[User]

}
