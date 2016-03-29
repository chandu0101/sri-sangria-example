package sri.sangria.mongoserver.exceptions

abstract private[exceptions]class BaseException(val msg: String,val name : String) extends Exception(msg)
