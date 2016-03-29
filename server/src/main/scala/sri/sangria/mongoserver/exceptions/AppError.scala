package sri.sangria.mongoserver.exceptions

case class AppError(errorScope : String,errorDetails : String,contact: String = "iDontKnow@nyThing.com")
