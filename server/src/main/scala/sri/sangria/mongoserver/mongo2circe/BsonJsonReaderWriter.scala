package sri.sangria.mongoserver.mongo2circe

import io.circe.{JsonObject, Json}
import reactivemongo.bson._

trait BsonJsonReaderWriter {

  /**
   * Writer
   */
  implicit val writer = new BSONDocumentWriter[Json] {
    override def write(t: Json): BSONDocument = {
      BSONDocument(t.asObject.get.toMap.map { case (k, v) => k -> convertJsonToBSON(v)})
    }

    private def convertJsonToBSON(in: Json): BSONValue = in match {
      case s: Json if (s.isString) => BSONString(s.asString.get)
      case n: Json if (n.isNumber) => BSONDouble(n.asNumber.get.toDouble)
      case b: Json if (b.isBoolean) => BSONBoolean(b.asBoolean.get)
      case arr: Json if (arr.isArray) => BSONArray(arr.asArray.get.map(convertJsonToBSON))
      case nul: Json if (nul.isNull) => BSONNull
      case obj: Json if (obj.isObject) => handleEdgeCases(obj.asObject.get)
    }

    private def handleEdgeCases(obj: JsonObject): BSONValue = {
      if (obj.fields.length > 1) writeObject(obj)
      else {
        obj.fields.head match {
          case $OID => BSONObjectID(obj($OID).get.asString.get)
          case $DATE_TIME => BSONDateTime(obj($DATE_TIME).get.asNumber.get.toLong.get)
          case _ => writeObject(obj)
        }
      }
    }

    private def writeObject(obj: JsonObject): BSONValue = BSONDocument(obj.toMap.map { case (k, v) => k -> convertJsonToBSON(v)})

  }

  /**
   * Reader
   */
  implicit val reader = new BSONDocumentReader[Json] {
    override def read(bson: BSONDocument): Json = Json.obj(bson.elements.toSeq.map { case (k, v) => (k, convertBsonToJson(v))}: _*)

    private def convertBsonToJson(in: BSONValue): Json = in match {
      case BSONString(value) => Json.string(value)
      case BSONInteger(value) => Json.int(value)
      case BSONDouble(value) => Json.number(value).get
      case BSONLong(value) => Json.long(value)
      case oid: BSONObjectID => Json.obj($OID -> Json.string(oid.stringify))
      case dt: BSONDateTime => Json.obj($DATE_TIME -> Json.long(dt.value))
      case BSONBoolean(true) => Json.True
      case BSONBoolean(false) => Json.False
      case arr: BSONArray => Json.array(arr.values.toSeq.map(convertBsonToJson): _*)
      case obj: BSONDocument => Json.obj(obj.elements.toSeq.map { case (k, v) => (k, convertBsonToJson(v))}: _*)
      case BSONNull => Json.Empty
    }
  }

}
