import sbt._

name := "streamapp-a"
version := "0.2"
scalaVersion := "2.13.0"

val root = (project in file("."))

val app1 = (project in file("app1")).
  settings(
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(

      // Testing Libraries
      "junit" % "junit" % "4.12" % Test,
      "org.scalatest" %% "scalatest" % "3.0.3" % Test,
      "org.scalacheck" %% "scalacheck" % "1.14.0",

      "io.spray" %%  "spray-json" % "1.3.5" withSources(),

      // Confluent Kafka libraries for Avro serialization & Schema management
      "io.confluent" % "kafka-schema-registry-client" % "3.3.0" withSources() withJavadoc(),
      "io.confluent" % "kafka-avro-serializer" % "3.3.0" withSources() withJavadoc(),
      "io.confluent" % "kafka-streams-avro-serde" % "5.3.0" withSources() withJavadoc(),

      // Akka HTTP dependencies for REST capabilities
      "com.typesafe.akka" %% "akka-actor" % "2.5.25",
      "com.typesafe.akka" %% "akka-http" % "10.1.9",
      "com.typesafe.akka" %% "akka-stream" % "2.5.25",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.9" ,
      "com.typesafe.akka" %% "akka-testkit" % "2.5.25" % Test,

      // Kafka Streams libraries for microservices development
      "org.apache.kafka" % "kafka-streams" % "2.3.0",
      "org.apache.kafka" % "kafka-streams-test-utils" % "2.3.0" % Test,

      // slf4j, log4j bridging libraries
      "org.slf4j" % "jul-to-slf4j" % "1.7.28",
      "org.slf4j" % "slf4j-log4j12" % "1.7.28"
    ),
    dependencyOverrides ++= Seq(
      "com.fasterxml.jackson.core" % "jackson-core" % "2.6.7",
      "com.fasterxml.jackson.core" % "jackson-annotations" % "2.6.7",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.6.7.1" % "provided",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7.1" % "provided"
    ),
    resolvers += "confluent" at "https://packages.confluent.io/maven/"
  )