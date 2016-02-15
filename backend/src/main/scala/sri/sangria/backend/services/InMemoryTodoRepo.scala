package sri.sangria.backend.services

import java.util.concurrent.atomic.AtomicInteger

import sri.sangria.backend.model.{Todo, User}


class InMemoryTodoRepo extends TodoRepo {

  val nextShipId = new AtomicInteger(11)

  val user = User("1", (1 to 10).map(_.toString).toSeq)
  var todos: Vector[Todo] = (1 to 10).map(i => Todo(i.toString, s"todo $i")).toVector

  def addTodo(text: String) = {
    val newTodo = Todo(s"${nextShipId.getAndIncrement}", text)
    todos = todos :+ newTodo
    newTodo
  }

  def getTodo(id: String) = todos.find(_.id == id)

  def getUser() = Some(user)
}