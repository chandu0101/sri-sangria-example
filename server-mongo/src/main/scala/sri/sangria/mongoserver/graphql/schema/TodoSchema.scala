package sri.sangria.mongoserver.graphql.schema

import sangria.relay._
import sangria.schema._
import sri.sangria.mongoserver.exceptions.MutationException
import sri.sangria.mongoserver.graphql.TodoRepo
import sri.sangria.mongoserver.util.CirceUtils
import sri.sangria.mongoserver.models.{AddTodoInput, Todo, User}
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

  val ConnectionDefinition(todoEdge, todoConnection) = Connection.definition[TodoRepo, Connection, Todo]("Todo", TodoType)


  val UserType: ObjectType[TodoRepo, User] = ObjectType(
    "User",
    "User object",
    interfaces[TodoRepo, User](nodeInterface),
    fields[TodoRepo, User](
      Node.globalIdField[TodoRepo, User],
      Field("todos", todoConnection,
        arguments = Connection.Args.All,
        resolve = ctx => ctx.ctx.getTodos(ctx)
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
      Field("todoEdge", todoEdge, resolve = ctx ⇒ ctx.ctx.getTodo(ctx.value.todoId).map(ot => Edge(ot.getOrElse(null), ctx.value.todoId))) ,
      Field("viewer", OptionType(UserType), resolve = _.ctx.getUser())
    ),
    mutateAndGetPayload = (input, ctx) ⇒ {
      ctx.ctx.addTodo(input.text).map(newTodo => AddTodoMutationPayload(input.clientMutationId.get, newTodo))
    }
  )

  val MutationType = ObjectType("Mutation", fields[TodoRepo, Unit](addTodoMutation))

  val schema = Schema(QueryType, Some(MutationType))
}