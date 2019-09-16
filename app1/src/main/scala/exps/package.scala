import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, PrettyPrinter}
import java.time.{Clock, Instant, ZoneId}

package object exps extends SprayJsonSupport with DefaultJsonProtocol {

  import spray.json.RootJsonFormat

  final case class AkkaServerDescription(name: String, version: String)

  object AkkaServerDescription {
    def apply(name: String, version: String): AkkaServerDescription = new AkkaServerDescription(name, version)

    implicit val printer = PrettyPrinter

    implicit def jsonFormatter: RootJsonFormat[AkkaServerDescription] =
      jsonFormat2[String, String, AkkaServerDescription](AkkaServerDescription.apply)
  }



  /*final case class Order(customerid: Int, itemids: List[Int], txnts: Long)

  case class Product(name: String, manufacturerCode: Long)

  final case class OrderD(id: String, customerId: Long, orderState: OrderState,
                          product: Product, quantity: Int, unitPrice: Double)

  object Order {
    def apply(customerid: Int, itemids: List[Int], txnts: Long) = new Order(customerid: Int,
                                                                              itemids: List[Int],
                                                                              txnts: Long)

    implicit def jsonFormatter: RootJsonFormat[Order] = jsonFormat3[Int, List[Int], Long, Order](Order.apply)
  }*/

  // Just for experimentation with implicit conversions
  case class A(elem: String)
  case class B(elem: Int)
  implicit def convertAtoB(in:A): B = B(Integer.parseInt(in.elem))

}
