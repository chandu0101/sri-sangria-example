package sri.sangria.server.exceptions

/**
 *  thrown when input request is not valid
 * @param message
 */
case class RequestException(message: String) extends Exception(message)
