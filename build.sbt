import sbt._


name := "streamapp-a"
version := "0.2"
scalaVersion := "2.13.0"

val root = (project in file("."))

val app1 = (project in file("app1")).
  settings(
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.12" % Test,
      "org.scalatest" %% "scalatest" % "3.0.3" % Test,
      "org.scalacheck" %% "scalacheck" % "1.14.0",
      "io.spray" %%  "spray-json" % "1.3.5",
      "io.confluent" % "kafka-schema-registry-client" % "3.3.0" withSources() withJavadoc(),
      "io.confluent" % "kafka-avro-serializer" % "3.3.0" withSources() withJavadoc(),
      "io.confluent" % "kafka-streams-avro-serde" % "5.3.0" withSources() withJavadoc(),
      "org.apache.kafka" % "kafka-streams" % "2.3.0",
      "org.apache.kafka" % "kafka-streams-test-utils" % "2.3.0" % Test,
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
    ),
    dependencyOverrides ++= Seq(
      "com.fasterxml.jackson.core" % "jackson-core" % "2.6.7",
      "com.fasterxml.jackson.core" % "jackson-annotations" % "2.6.7",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.6.7.1" % "provided",
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7.1" % "provided"
    ),
    resolvers += "confluent" at "https://packages.confluent.io/maven/"
  )