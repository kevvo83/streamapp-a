import spray.json._

object sess {

  //import exps.{A, B}

  val x: JsValue = """{"key":1, "value":"abcd"}""".parseJson
  x.prettyPrint

  //val yll: JsValue = List(1, 2, 3).toJson(listFormat)
  //List(1, 2, 3)

  //yll.convertTo[List[Int]](listFormat)

  // Check of implicit conversions
  //val firstA: A = A("111")
  //val firstB: B = firstA
  //firstB.elem

}


