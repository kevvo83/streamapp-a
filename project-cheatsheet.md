# Kafka CheatSheet


### REST Calls to Akka-Http webserver

#### POST an Order to the /orders endpoint

```
curl --header "Content-Type: application/json" \
   --request POST \
   --data '{"id":"1000", "customerId":999299191, "orderState":{"currentState":"INIT"}, "product":{"id":"2", "manufacturer":"Bosch", "countryOfOrigin":"Germany"}, "quantity":1, "unitPrice":20.1}' \
   http://localhost:9089/orders
```


### Kafkacat Reference 

#### List all topics and their partitions
`kafkacat -b localhost:9092 -L`

####


## Schema Registry Reference

#### REST Endpoints Reference
[Confluent Website](https://docs.confluent.io/current/schema-registry/develop/api.html)


#### What is a Subject in the Schema Registry?

A subject refers to the name in which the Schema is registered. For eg., a subject refers to _topicname-key_ or _topicname-value_.


#### Query all subjects in the Schema Registry

`curl -X GET  "http://localhost:8081/subjects"`






