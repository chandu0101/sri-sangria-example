package sri.sangria.mongoserver.services

import reactivemongo.bson.BSONObjectID
import sangria.relay.{ConnectionArgs, Connection}
import sangria.schema.{WithArguments}
import sri.sangria.mongoserver.db.MongoContext
import sri.sangria.mongoserver.models.{User, Todo}
import sri.sangria.mongoserver.mongo2circe._
import io.circe.generic.auto._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.{Await, Future}

class TodoService extends BaseService[Todo](MongoContext.db, "todos") {


  def addTodo(text: String): Future[BSONObjectID] = {
    val todo = Todo(text = text)
    insert(todo).map(wr => handleWriteResult(wr, todo._id))
  }

  def getTodo(id: BSONObjectID): Future[Option[Todo]] = findById(id)


  def getTodos(args: WithArguments): Future[Connection[Todo]] = {
    relayConnection(args = ConnectionArgs(args))
  }
}
