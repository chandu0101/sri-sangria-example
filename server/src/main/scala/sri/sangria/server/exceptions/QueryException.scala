package sri.sangria.server.exceptions

/**
 *  thrown when input query is not valid
 * @param message
 */
case class QueryException(message: String) extends Exception(message)
