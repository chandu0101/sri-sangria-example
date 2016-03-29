package sri.sangria.mongoserver.services

import io.circe.{Json, Encoder, Decoder}
import reactivemongo.api.{QueryOpts, DefaultDB}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import sangria.relay._
import io.circe.generic.auto._
import sri.sangria.mongoserver.exceptions.DatabaseException
import sri.sangria.mongoserver.mongo2circe.BsonJsonReaderWriter
import sri.sangria.mongoserver.util.{CirceUtils}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BaseService[T](db: => DefaultDB, collectionName: String)(implicit encoder: Encoder[T], decoder: Decoder[T]) extends BsonJsonReaderWriter {

  def collection = db[BSONCollection](collectionName)

  /**
   * insert a new document
   * @param entity
   * @return
   */
  def insert(entity: T): Future[WriteResult] = collection.insert(encoder(entity))

  /**
   * find the document based on id
   * @param id
   * @return
   */
  def findById(id: BSONObjectID): Future[Option[T]] = collection.find(BSONDocument("_id" -> id)).one[Json].map(_.map(j => {
    val tXor = j.as[T]
    tXor.getOrElse(throw new DatabaseException(s"Error decoding entity : ${CirceUtils.getCirceErrorMessage(tXor)}"))
  }))

  /**
   * returns number of documents in collection for a given selector
   * @param selector
   * @return
   */
  def count(selector: BSONDocument = BSONDocument.empty): Future[Int] = collection.count(Some(selector))


  /**
   * returns  relay Connection object
   * @param selector
   * @param sort
   * @param args
   * @return
   */
  def relayConnection(selector: BSONDocument = BSONDocument.empty, sort: BSONDocument = BSONDocument("_id" -> -1), args: ConnectionArgs): Future[Connection[T]] = {
    import args._

    def getOffset(cursor: Option[String], default: Int) = cursor flatMap (Connection.cursorToOffset) getOrElse (default)

    count(selector).flatMap(size => {

      val beforeOffset = getOffset(before, size)
      val afterOffset = getOffset(after, -1)

      val startOffsetTemp = Math.max(-1, afterOffset) + 1
      val endOffsetTemp = Math.min(size, beforeOffset)

      val endOffset = first.fold(endOffsetTemp)(f => Math.min(endOffsetTemp, startOffsetTemp + f))
      val startOffset = last.fold(startOffsetTemp)(l => Math.max(startOffsetTemp, endOffset - l))

      val skip = Math.max(startOffset, 0)
      val limit = endOffset - startOffset

      val result: Future[Vector[T]] = if (limit == 0) Future.successful(Vector())
      else collection.find(selector).options(QueryOpts().skip(skip)).sort(sort).cursor().collect[Vector](limit).map(v => {
        val vtXor = Json.array(v: _*).hcursor.as[Vector[T]]
        vtXor.getOrElse(throw new DatabaseException(s"Error decoding entities list : ${CirceUtils.getCirceErrorMessage(vtXor)}"))
      })

      result.map(
        nodes => {
          val edges = nodes.zipWithIndex.map {
            case (value, index) => Edge(value, Connection.offsetToCursor(startOffset + index))
          }

          val firstEdge = edges.headOption
          val lastEdge = edges.lastOption
          val lowerBound = after.fold(0)(_ ⇒ afterOffset + 1)
          val upperBound = before.fold(size)(_ ⇒ beforeOffset)

          DefaultConnection(
            PageInfo(
              startCursor = firstEdge map (_.cursor),
              endCursor = lastEdge map (_.cursor),
              hasPreviousPage = last.fold(false)(_ ⇒ startOffset > lowerBound),
              hasNextPage = first.fold(false)(_ ⇒ endOffset < upperBound)),
            edges
          )
        }
      )
    })
  }


  /**
   * handy method to handle WriteResult Errors
   * @param wr
   * @param id
   * @return
   */
  def handleWriteResult(wr: WriteResult, id: BSONObjectID) = {
    if (wr.inError) throw new DatabaseException(wr.errmsg.get)
    else id
  }

}
