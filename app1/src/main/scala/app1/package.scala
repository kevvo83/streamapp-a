import spray.json._

package object app1 {
  case class SensorData(val ts: Long, sensorid: String, pressure_data: Double)

  object SensorDataJsonProtocol extends DefaultJsonProtocol {
    implicit val sensorDataJsonFormat = jsonFormat3(SensorData)
  }

  val sensorIdList: List[String] = List(
    "900010",
    "900012",
    "900112",
    "900122"
  )

}
