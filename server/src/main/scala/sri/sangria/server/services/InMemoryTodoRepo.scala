package sri.sangria.server.services

import java.util.concurrent.atomic.AtomicInteger

import sangria.relay.ConnectionArgs
import sangria.schema.Args
import sri.sangria.server.models.{Todo, User}

import scala.concurrent.Future


class InMemoryTodoRepo extends TodoRepo {

  val nextTodoId = new AtomicInteger(11)

  var user = User("1", (1 to 10).map(_.toString).toSeq)
  var todos: Vector[Todo] = (1 to 10).map(i => Todo(i.toString, s"todo $i")).toVector

  def addTodo(text: String) = {
    val newID = nextTodoId.getAndIncrement
    val newTodo = Todo(s"${newID}", text)
    todos = todos.+:(newTodo)
    user = user.copy(todos = user.todos.+:(newID.toString))
    newTodo
  }

  def getTodo(id: String) = todos.find(_.id == id)

  def getUser() = Some(user)

  override def getTodos(ids: Seq[String],args : Args) = {
    println(s"go for it $args")
    Future.successful(ids.map(getTodo))
  }
}