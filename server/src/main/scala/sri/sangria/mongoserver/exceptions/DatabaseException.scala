package sri.sangria.mongoserver.exceptions

/**
 *  exception occurred in DB layer
 * @param message
 */
case class DatabaseException(message: String) extends BaseException(msg = message,name = "database")
