package sri.sangria.mongoserver.models

import reactivemongo.bson.BSONObjectID
import sangria.relay.{Identifiable, Node}

case class Todo(_id: BSONObjectID = BSONObjectID.generate, text: String)

object Todo {

  implicit object TodoNo extends Identifiable[Todo] {
    override def id(value: Todo): String = value._id.stringify
  }

}
