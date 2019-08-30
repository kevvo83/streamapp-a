package app1

import java.io.File
import org.apache.avro.Schema
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import com.typesafe.scalalogging.LazyLogging
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.kafka.common.header.internals.{RecordHeader, RecordHeaders}

object MainProducer extends App with RandomSensorData with KafkaClusterConfig with LazyLogging {

  val gf: String = if (args.length > 0) args(0) else ""
  val pf: String = if (args.length > 1) args(1) else ""

  val kafkaClusterConfig = new KafkaClusterConfig(gf, pf)

  logger.info(s"Initiating Message Transfers to Topic: ${kafkaClusterConfig.
                                                            producerProps.getProperty("IOTRAWDATATOPIC")}")

  var kp: KafkaProducer[GenericRecord, GenericRecord] = null

  val val_schema: Schema = new Schema.Parser().
                                  parse(new File(getClass().getResource("/app1/RawSensorData_v.avsc").toURI))
  val key_schema: Schema = new Schema.Parser().
                                  parse(new File(getClass.getResource("/app1/RawSensorData_k.avsc").toURI))

  try {
    kp = new KafkaProducer[GenericRecord, GenericRecord](kafkaClusterConfig.kpProps)

    randomSensorData foreach (sensor_d => {

      val val_r: GenericRecord = new GenericData.Record(val_schema)
      val key_r: GenericRecord = new GenericData.Record(key_schema)

      val valuesmap: Map[String, Any] = SensorDataToMapValues(sensor_d)
      for (key <- valuesmap.keys) val_r.put(key, valuesmap.get(key).get)

      //val_r.put("ts", sensor_d.ts)
      //val_r.put("pressure_data", sensor_d.pressure_data)
      //val_r.put("sensorid", sensor_d.sensorID)

      key_r.put("sensorid", sensor_d.sensorid)

      val kr = new ProducerRecord[GenericRecord, GenericRecord](
                                                kafkaClusterConfig.producerProps.getProperty("IOTRAWDATATOPIC"),
                                                null, key_r, val_r,
                                                new RecordHeaders().add(new RecordHeader(
                                                                                "sourcesystem",
                                                                                "app1.RandomSensorData".getBytes))
                                                                    .add(new RecordHeader(
                                                                                "targetsystem",
                                                                                "ccloud".getBytes))
                                                )

      kp.send(kr)

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
    })
  } catch {
    case (e: Exception) => {
      logger.info(s"${e.getCause}")
      logger.info(s"${e.getMessage}")
      logger.info("Stack Trace:\n")
      e.getStackTrace foreach (line => logger.info(s"${line}"))
    }
  } finally {
    if (kp != null) kp.close()
  }

  logger.info("Completed Message Transfers")

}
