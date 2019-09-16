package exps

import spray.json.RootJsonFormat

case class OrderState(currentState: String)

object OrderState {
  val INIT = OrderState("INIT")
  val VALIDATED = OrderState("VALIDATED")
  val COMPLETED = OrderState("COMPLETED")
  val FAILED = OrderState("FAILED")

  implicit def jsonFormatter: RootJsonFormat[OrderState] =
    jsonFormat1[String, OrderState](OrderState.apply)
}
