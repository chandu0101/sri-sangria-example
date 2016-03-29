package sri.sangria.mongoserver.db

import reactivemongo.api.MongoDriver

import scala.concurrent.ExecutionContext.Implicits.global


object MongoContext {

  val driver = new MongoDriver()
  val servers = List("localhost")
  val connection = driver.connection(servers)

  def db = connection("test")
}
