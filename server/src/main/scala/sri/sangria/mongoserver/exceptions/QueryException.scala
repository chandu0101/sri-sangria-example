package sri.sangria.mongoserver.exceptions

/**
 *  thrown when input query is not valid
 * @param message
 */
case class QueryException(message: String) extends BaseException(message,"query")
