package exps

import java.io.FileInputStream
import java.util
import java.util.{Properties => juProps}

import io.confluent.kafka.schemaregistry.client.rest.RestService
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.config.SaslConfigs

trait KafkaClusterConfig {

  val kafkaClusterConfig: KafkaClusterConfig

  class KafkaClusterConfig(val genericPropertiesFile: String, val producerPropertiesFile: String) {

    assert(genericPropertiesFile != "")
    assert(producerPropertiesFile != "")

    lazy val props: juProps = new juProps()
    props.load(new FileInputStream(genericPropertiesFile))

    lazy val producerProps: juProps = new juProps()
    producerProps.load(new FileInputStream(producerPropertiesFile))

    // Kafka Common Properties
    lazy val kcProps: juProps = new juProps()
    kcProps.put(CommonClientConfigs.CLIENT_ID_CONFIG, "blahblah")
    kcProps.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, props.getProperty("CCLOUD_BROKERS"))
    kcProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL")
    kcProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN")
    kcProps.put(SaslConfigs.SASL_JAAS_CONFIG,
      s"""org.apache.kafka.common.security.plain.PlainLoginModule """ +
        s"""required username="${props.getProperty("CCLOUD_ACCESS_KEY_ID")}" """ +
        s"""password="${props.getProperty("CCLOUD_SECRET_ACCESS_KEY")}";"""
    )

    // Schema Registry Properties
    kcProps.put("schema.registry.url", props.getProperty("CCLOUD_SCHEMA_REGISTRY"))
    kcProps.put("basic.auth.credentials.source", "USER_INFO")
    kcProps.put("basic.auth.user.info", s"""${props.getProperty("CCLOUD_SCHEMA_REGISTRY_ACCESS_KEY_ID")}:""" +
      s"""${props.getProperty("CCLOUD_SCHEMA_REGISTRY_SECRET_ACCESS_KEY")}""")

    // Kafka Producer Properties
    lazy val kpProps: juProps = new juProps()
    kpProps.putAll(kcProps)
    kpProps.put(ProducerConfig.ACKS_CONFIG, "all")
    kpProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
    kpProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])

    // Schema Registry Rest API
    val rs1: RestService = new RestService(s"${props.getProperty("CCLOUD_SCHEMA_REGISTRY")}")
    val headers = new util.HashMap[String, String]()
    headers.put("Authorization","Basic " +
      util.Base64.
        getEncoder().
        encodeToString(s"""${kcProps.getProperty("basic.auth.user.info")}""".getBytes()))
  }

}
