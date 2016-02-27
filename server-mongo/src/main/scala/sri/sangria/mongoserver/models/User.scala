package sri.sangria.mongoserver.models

import sangria.relay.Node

case class User(id: String, todos: Seq[String]) extends Node
