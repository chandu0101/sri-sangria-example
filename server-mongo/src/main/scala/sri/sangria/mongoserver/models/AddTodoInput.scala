package sri.sangria.mongoserver.models

case class AddTodoInput(text : String,clientMutationId: Option[String])
