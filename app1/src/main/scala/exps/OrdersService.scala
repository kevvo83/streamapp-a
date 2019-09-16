package exps

import java.io.File

import akka.http._
import akka.http.scaladsl._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.slf4j.{Logger, LoggerFactory}
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.common.header.internals.{RecordHeader, RecordHeaders}

import scala.util.{Failure, Success, Try}
import scala.io.StdIn
import java.time.{Clock, Instant, ZoneId}


object OrdersService extends App with KafkaClusterConfig {

  val gp: String = args(0)
  val pp: String = args(1)

  val kafkaClusterConfig: KafkaClusterConfig = new KafkaClusterConfig(gp, pp)

  implicit val system = ActorSystem("akka-http-rest-server")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  System.setProperty("log4j.configuration",getClass.getResource("../log4j.properties").toString)
  final val logger: Logger = LoggerFactory.getLogger("OrdersService")

  val ordersServiceHost = "localhost"
  val ordersServicePort = 9089

  var kp: KafkaProducer[GenericRecord, GenericRecord] =
    new KafkaProducer[GenericRecord, GenericRecord](kafkaClusterConfig.kpProps)

  val val_schema: Schema = new Schema.Parser().
                              parse(new File(getClass().getResource("/exps/Order_v.avsc").toURI))
  val key_schema: Schema = new Schema.Parser().
                              parse(new File(getClass.getResource("/exps/Order_k.avsc").toURI))

  val routes: Route = {
    path("orders") {
      post {
        entity(as[Order]) {
          specificorder => {

            // Build the Generic Avro header
            val val_r: GenericRecord = new GenericData.Record(val_schema)
            val key_r: GenericRecord = new GenericData.Record(key_schema)

            //val valuesmap: Map[String, Any] = SensorDataToMapValues(sensor_d)
            //for (key <- valuesmap.keys) val_r.put(key, valuesmap.get(key).get)

            key_r.put("sensorid", specificorder.id)

            val kr = new ProducerRecord[GenericRecord, GenericRecord](
                                                            kafkaClusterConfig.producerProps.getProperty("ORDERTOPIC"),
                                                            null, key_r, val_r,
                                                            new RecordHeaders().add(new RecordHeader(
                                                                                      "sourcesystem",
                                                                                      "OrdersService".getBytes))
                                                                                      .add(new RecordHeader(
                                                                                        "targetsystem",
                                                                                        "ccloud".getBytes)))

            kp.send(kr, new Callback() {
              override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
                if (exception != null) {
                  logger.info(s"${exception.getCause}\n")
                  exception.getStackTrace foreach (line => logger.info(s"\t\t${line}"))
                  logger.info(s"${exception.getMessage}")
                }
                else logger.info(s"The new offset is ${metadata.offset()}, and is sent " +
                  s"to partition ${metadata.partition()} - ${metadata.toString}")
              }
            })

            complete(HttpResponse(StatusCodes.OK,
                                  Nil,
                                  HttpEntity(ContentTypes.`application/json`,s"""""")))
          }
        }
      }
    } ~
    path("") {
      get {
        val clock: Clock = Clock.system(ZoneId.of("GMT"))
        val time: String = Instant.now(clock).toString
        complete(HttpEntity(ContentTypes.`application/json`, time))
      }
    }
  }

  val httpServerFuture = Http().bindAndHandle(routes, ordersServiceHost, ordersServicePort)

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
