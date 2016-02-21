package sri.sangria.server.services

import sri.sangria.server.models.{Todo, User}

trait TodoRepo {

  def addTodo(text: String): Todo

  def getTodo(id: String): Option[Todo]

  def getUser(): Option[User]

}
