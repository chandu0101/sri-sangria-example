package sri.sangria.backend.model

import sangria.relay.Node

case class User(id: String, todos: Seq[String]) extends Node
