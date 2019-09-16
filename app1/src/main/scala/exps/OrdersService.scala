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
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.slf4j.{Logger, LoggerFactory}
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.common.header.internals.{RecordHeader, RecordHeaders}


class OrdersService extends App with KafkaClusterConfig {

  val gp: String = args(0)
  val pp: String = args(1)

  val kafkaClusterConfig: KafkaClusterConfig = new KafkaClusterConfig(gp, pp)

  implicit val system = ActorSystem("akka-http-rest-server")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  System.setProperty("log4j.configuration",getClass.getResource("../log4j.properties").toString)
  final val logger: Logger = LoggerFactory.getLogger("OrdersService")

  val host = "localhost"
  val port = 9092

  var kp: KafkaProducer[GenericRecord, GenericRecord] =
    new KafkaProducer[GenericRecord, GenericRecord](kafkaClusterConfig.kpProps)

  val val_schema: Schema = new Schema.Parser().
                              parse(new File(getClass().getResource("/app1/Order_v.avsc").toURI))
  val key_schema: Schema = new Schema.Parser().
                              parse(new File(getClass.getResource("/app1/Order_k.avsc").toURI))

  val routes: Route = {
    path("/orders") {
      post{
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
                "app1.RandomSensorData".getBytes))
                .add(new RecordHeader(
                  "targetsystem",
                  "ccloud".getBytes))
            )


            complete(HttpResponse(StatusCodes.Created,
                                  Nil,
                                  HttpEntity(ContentTypes.`application/json`,s"""""")))
          }
        }
      }
    }
  }

}
