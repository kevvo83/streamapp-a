package app1

import java.time.{Clock, Instant, ZoneId}

import org.scalacheck.Gen

trait RandomSensorData {

  val clock: Clock = Clock.system(ZoneId.of("GMT"))

  val x: Gen[SensorData] = for {
    b <- Gen.oneOf[String](sensorIdList)
    c <- Gen.choose[Double](20.0, 100.0)
  } yield SensorData(Instant.now(clock).getEpochSecond, b, c)

  val defaultIters: String = "20"
  val totalNumIters: Int = Integer.parseInt(Option(System.getenv("num.samples")).getOrElse[String](defaultIters))

  val randomSensorData: Stream[SensorData] = (((1 to totalNumIters) toStream)) map(elem => x.sample.get)

  val listOfSensorDataFields: List[String] = (for (a <- SensorData.getClass.getDeclaredFields) yield a.getName).toList

  def SensorDataToMapValues(input: SensorData): Map[String, Any] = {
    val fieldNames = input.getClass.getDeclaredFields.map(_.getName)
    val fieldVals =  SensorData.unapply(input).get.productIterator.toSeq
    fieldNames.zip(fieldVals) toMap
  }

  //(1 #:: Stream(2,totalNumIters)) map[SensorData] (e => x.sample.get)

}
