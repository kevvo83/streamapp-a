package app1

object SchemaRegistryClient extends App with KafkaClusterConfig {

  val gf: String = if (args.length > 0) args(0) else ""
  val pf: String = if (args.length > 1) args(1) else ""

  // Purely to verify that the supplied connect configurations work

  val kafkaClusterConfig = new KafkaClusterConfig(gf, pf)

  val allSubjects = kafkaClusterConfig.rs1.getAllSubjects(kafkaClusterConfig.headers)

  allSubjects.forEach(println(_))

  assert(allSubjects contains ("kev-test-1-key"))
  assert(allSubjects contains ("kstreamprodtest-key"))

}
