package exps

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import org.slf4j.{Logger, LoggerFactory}

import scala.io.StdIn
import scala.util.{Failure, Success, Try}


object WebServ extends App {

  implicit val system = ActorSystem("akka-http-rest-server")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  System.setProperty("log4j.configuration",getClass.getResource("../log4j.properties").toString)
  final val logger: Logger = LoggerFactory.getLogger(WebServ.getClass)

  val host = "localhost"
  val port = 9099

  val routes: Route = concat(
    path("") {
      get {
        complete("Akka Server is up")
      }
    },
    path("server-version") {
      get {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "1.0.0.0"))
      }
    },
    path("server-version-json-encoding") {
      get {
        val server: AkkaServerDescription = AkkaServerDescription("TestWebServer1", "2.0.0")
        complete(server)
      }
    },
    path("enter-order-details") {
      post {
        entity(as[Order]) { specificorder: Order => {
            logger.info(s"Txn entered ${specificorder.customerid}, ${specificorder.itemids}")
            complete(StatusCodes.Created)
          }
        }
      }
    }
  )

  val server: AkkaServerDescription = AkkaServerDescription("TestWebServer1", "2.0.0")

  val httpServerFuture = Http().bindAndHandle(routes, host, port)

  httpServerFuture onComplete[Unit] (x => {
    x match {
      case Success(serverBinding) => logger.info(s"HTTP Server is Up and bound to ${serverBinding}")
      case Failure(exp) => {
        logger.info(s"HTTP Server could not startup due to: ${exp.getCause}")
      }
    }
  })

  StdIn.readLine() // Does 'Enter' terminate the string read from the Terminal? - Oh yeah, maybe it does!
  httpServerFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())

}
