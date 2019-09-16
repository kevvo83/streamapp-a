package exps

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.RootJsonFormat

final case class Order(id: String, customerId: Long, orderState: OrderState,
                       product: Product, quantity: Int, unitPrice: Double) extends SprayJsonSupport

object Order {

  def apply(id: String, customerId: Long, orderState: OrderState,
            product: Product, quantity: Int, unitPrice: Double) = {
    new Order(id, customerId, orderState, product, quantity, unitPrice)
  }

  implicit def jsonFormatter: RootJsonFormat[Order] =
    jsonFormat6[String, Long, OrderState, Product, Int, Double, Order](Order.apply)

}


