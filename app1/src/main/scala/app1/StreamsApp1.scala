package app1

object StreamsApp1 extends App with KafkaClusterConfig {

  val gf: String = if (args.length > 0) args(0) else ""
  val pf: String = if (args.length > 1) args(1) else ""

  val kafkaClusterConfig: KafkaClusterConfig = new KafkaClusterConfig(gf, pf)

  // Load all the IOT Sensor data into a Topic using Kafka Source Connect! - ideally from a Datastore like Cassandra!
  // Setup a Connection Pool using HikariCP, and then use a library like Slick to insert records into the DB.
  // Checkout how all of that stuff works!!! - and how to fucking Monitor it????


  // Build the Streams Application KStreams, KTable, etc.
  // Need to read on Joins, GlobalKTable - wtf are these???


}
