package sri.sangria.mongoserver.services

import io.circe.Json
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import sangria.relay.{ConnectionArgs, Connection}
import sangria.schema.{WithArguments, Args}
import sri.sangria.mongoserver.db.MongoContext
import sri.sangria.mongoserver.exceptions.DatabaseException
import sri.sangria.mongoserver.graphql.TodoRepo
import sri.sangria.mongoserver.models.{User, Todo}
import sri.sangria.mongoserver.mongo2circe._
import io.circe.generic.auto._
import sri.sangria.mongoserver.util.FutureUtils
import scala.concurrent.duration._
import io.circe.syntax._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import scala.concurrent.{Await, Future}

class TodoService extends BaseService[Todo](MongoContext.db, "todos") {


  def addTodo(text: String): Future[BSONObjectID] = {
    val todo = Todo(text = text)
    insert(todo).map(wr => handleWriteResult(wr, todo._id))
  }

  def getTodo(id: BSONObjectID): Option[Todo] = {
    FutureUtils.awaitFuture(findById(id))
  }

  def getTodos(args: WithArguments): Future[Connection[Todo]] = {
    relayConnection(args = ConnectionArgs(args))
  }
}
