package sri.sangria.mongoserver.exceptions

/**
 *  exception occurred while handling mutations in schema
 * @param message
 */
case class MutationException(message: String) extends BaseException(msg = message,name = "mutation")
