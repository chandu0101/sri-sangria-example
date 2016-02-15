package sri.sangria.backend.services

import sri.sangria.backend.model.{Todo, User}

trait TodoRepo {

  def addTodo(text: String): Todo

  def getTodo(id: String): Option[Todo]

  def getUser(): Option[User]

}
