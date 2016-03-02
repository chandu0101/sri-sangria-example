package sri.sangria.server.schema

import sangria.relay._
import sangria.schema._
import sri.sangria.server
import sri.sangria.server.models.{AddTodoInput, Todo, User}
import sri.sangria.server.services.TodoRepo
import sangria.marshalling.circe._
import io.circe.generic.auto._
import scala.concurrent.ExecutionContext.Implicits.global


object TodoSchema {

  /**
   * We get the node interface and field from the relay library.
   *
   * The first method is the way we resolve an ID to its object. The second is the
   * way we resolve an object that implements node to its type.
   */
  val NodeDefinition(nodeInterface, nodeField) = Node.definition((id: GlobalId, ctx: Context[TodoRepo, Unit]) ⇒ {
    id.typeName match {
      case "User" => ctx.ctx.getUser()
      case "Todo" => ctx.ctx.getTodo(id.id)
      case _ => None
    }
  }, Node.possibleNodeTypes[TodoRepo, Node](UserType, TodoType))

  def idFields[T: Identifiable] = fields[Unit, T](
    Node.globalIdField,
    Field("rawId", StringType, resolve = ctx ⇒ implicitly[Identifiable[T]].id(ctx.value))
  )


  val TodoType: ObjectType[Unit, Todo] = ObjectType(
    "Todo",
    "Todo object skeleton",
    interfaces[Unit, Todo](nodeInterface),
    fields[Unit, Todo](
      Node.globalIdField[Unit, Todo],
      Field("text", StringType, Some("Todo item text"), resolve = _.value.text)
    )
  )

  val ConnectionDefinition(todoEdge, todoConnection) = Connection.definition[TodoRepo, Connection, Option[Todo]]("Todo", OptionType(TodoType))


  val UserType: ObjectType[TodoRepo, User] = ObjectType(
    "User",
    "User object",
    interfaces[TodoRepo, User](nodeInterface),
    fields[TodoRepo, User](
      Node.globalIdField[TodoRepo, User],
      Field("todos", todoConnection,
        arguments = Connection.Args.All,
        resolve = ctx => Connection.connectionFromFutureSeq(ctx.ctx.getTodos(ctx.value.todos, ctx.args), ConnectionArgs(ctx))
      )
    )
  )

  val QueryType = ObjectType("Query", fields[TodoRepo, Unit](
    Field("viewer", OptionType(UserType),
      resolve = ctx ⇒ ctx.ctx.getUser()),
    nodeField))

  case class AddTodoMutationPayload(clientMutationId: String, todoId: String) extends Mutation


  val addTodoMutation = Mutation.fieldWithClientMutationId[TodoRepo, Unit, AddTodoMutationPayload, AddTodoInput](
    fieldName = "addTodo",
    typeName = "AddTodo",
    inputFields = List(
      InputField("text", StringType)),
    outputFields = fields(
      Field("todoEdge", todoEdge, resolve = ctx ⇒ Edge(ctx.ctx.getTodo(ctx.value.todoId), ctx.value.todoId)),
      Field("viewer", OptionType(UserType), resolve = _.ctx.getUser())
    ),
    mutateAndGetPayload = (input, ctx) ⇒ {
      val newTodo = ctx.ctx.addTodo(input.text)
      AddTodoMutationPayload(input.clientMutationId.get, newTodo.id)
    }
  )

  val MutationType = ObjectType("Mutation", fields[TodoRepo, Unit](addTodoMutation))

  val schema = Schema(QueryType, Some(MutationType))
}