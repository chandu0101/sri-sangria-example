package sri.sangria.backend.schema

import sangria.relay._
import sangria.schema._
import sri.sangria.backend.model.{Todo, User}
import sri.sangria.backend.services.TodoRepo


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
        resolve = ctx => Connection.connectionFromSeq(ctx.value.todos map ctx.ctx.getTodo, ConnectionArgs(ctx))
      )
    )
  )

  val QueryType = ObjectType("Query", fields[TodoRepo, Unit](
    Field("viewer", OptionType(UserType),
      resolve = ctx ⇒ ctx.ctx.getUser()),
    nodeField))

  case class AddTodoMutationPayload(clientMutationId: String, todoId: String) extends Mutation


  val addTodoMutation = Mutation.fieldWithClientMutationId[TodoRepo, Unit, AddTodoMutationPayload, InputObjectType.DefaultInput](
    fieldName = "addTodo",
    typeName = "AddTodo",
    inputFields = List(
      InputField("text", StringType)),
    outputFields = fields(
      Field("todoEdge", todoEdge, resolve = ctx ⇒ Edge(ctx.ctx.getTodo(ctx.value.todoId), ctx.value.todoId)),
      Field("viewer", OptionType(UserType), resolve = _.ctx.getUser())
    ),
    mutateAndGetPayload = (input, ctx) ⇒ {
      val mutationId = input(Mutation.ClientMutationIdFieldName).asInstanceOf[String]
      val text = input("text").asInstanceOf[String]
      val newTodo = ctx.ctx.addTodo(text)
      AddTodoMutationPayload(mutationId, newTodo.id)
    }
  )

  val MutationType = ObjectType("Mutation", fields[TodoRepo, Unit](addTodoMutation))

  val schema = Schema(QueryType, Some(MutationType))
}