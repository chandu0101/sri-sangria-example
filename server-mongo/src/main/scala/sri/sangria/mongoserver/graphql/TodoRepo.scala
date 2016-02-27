package sri.sangria.mongoserver.graphql

import reactivemongo.bson.BSONObjectID
import sangria.relay.Connection
import sangria.schema.WithArguments
import sri.sangria.mongoserver.models.{Todo, User}
import sri.sangria.mongoserver.services.TodoService
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class TodoRepo {

  lazy val todoService = new TodoService

  def getUser(): Option[User] = Some(User("hello", null))

  def addTodo(text: String): Future[String] = todoService.addTodo(text).map(_.stringify)

  def getTodos(args: WithArguments): Future[Connection[Todo]] = todoService.getTodos(args)

  def getTodo(id: String): Option[Todo] = todoService.getTodo(BSONObjectID(id))


}
