import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.directives._
import spray.json.DefaultJsonProtocol

case class ServerDescriptor(name: String, version: String)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val domainJsonFormat = jsonFormat2[String, String, ServerDescriptor](ServerDescriptor)
}

object session extends JsonSupport {

  val newServer: ServerDescriptor = ServerDescriptor("NewHTTPServer", "1.0.0")

  RouteDirectives.complete(newServer)

}