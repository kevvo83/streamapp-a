package exps

trait GenerateCustomerInformation {
  this: KafkaClusterConfig =>
  def generateTopicData(topicname: String)
}
