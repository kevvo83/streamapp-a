package exps

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import java.time._

import spray.json.RootJsonFormat

final case class Product(id: String, manufacturer: String, countryOfOrigin: String) extends SprayJsonSupport

object Product {

  def apply(id: String, manufacturer: String, countryOfOrigin:String) =
    new Product(id, manufacturer, countryOfOrigin)

  implicit def jsonFormatter: RootJsonFormat[Product] =
    jsonFormat3[String, String, String, Product](Product.apply)

}
