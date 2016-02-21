package sri.sangria.server.models

case class AddTodoInput(text : String,clientMutationId: Option[String])
