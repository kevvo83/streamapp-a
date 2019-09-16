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


object WebServTest extends App with KafkaClusterConfig {

  implicit val system = ActorSystem("akka-http-rest-server")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val gprop: String = args(0)
  val pprop: String = args(1)

  val kafkaClusterConfig: KafkaClusterConfig = new KafkaClusterConfig(gprop, pprop)

  System.setProperty("log4j.configuration",getClass.getResource("../log4j.properties").toString)
  final val logger: Logger = LoggerFactory.getLogger(WebServTest.getClass)

  val host = "localhost"
  val port = 9089

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
            logger.info(s"Txn entered ${specificorder.id}, ${specificorder.customerId}")
            complete(StatusCodes.Created)
          }
        }
      }
    }
  )

  val routesNew: Route = {
    path("") {
      get {
        complete("Akka HTTP Server is Up")
      }
    } ~
    path("server-version") {
      get {
        val server: AkkaServerDescription = AkkaServerDescription("TestWebServer1 New Route!", "2.0.1")
        complete(HttpEntity(ContentTypes.`application/json`, server.toJson.compactPrint))
      }
    } ~
    path("server-version-pretty") {
      get {
        val server: AkkaServerDescription = AkkaServerDescription("TestWebServer1 Another New Route!", "2.0.2")
        complete(server)
      }
    } /*~
    path("/orders") {
      post {
        entity(as[OrderD]) { specificorder: OrderD => {

        }
        }
      }
    }*/
  }

  val server: AkkaServerDescription = AkkaServerDescription("TestWebServer1", "2.0.0")

  val httpServerFuture = Http().bindAndHandle(routesNew, host, port)

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
