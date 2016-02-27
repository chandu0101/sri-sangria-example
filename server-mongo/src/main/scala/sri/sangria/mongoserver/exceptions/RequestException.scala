package sri.sangria.mongoserver.exceptions

/**
 *  thrown when input request is not valid
 * @param message
 */
case class RequestException(message: String) extends BaseException(message,"request")
