package sri.sangria.server.exceptions

case class SriSangriaError(errorScope : String,message : String,contact: String = "iDontKnow@nyThing.com")
