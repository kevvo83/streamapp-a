import java.io.File
import scala.io.Source
import spray.json._

object session {

  val x = Source.fromFile(getClass.getResource("/exps/Order_k.avsc").toURI).getLines()

  val b = new StringBuilder
  while (x.hasNext) b.append(x.next())

  // Print the resultant string that can be used in the POST Rest call
  s"""'{"schema": "${b.toString().parseJson.compactPrint.replace("\"", "\\\"")}"}'"""


  val x1 = Source.fromFile(getClass.getResource("/exps/Order_v.avsc").toURI).getLines()

  val b1 = new StringBuilder
  while (x1.hasNext) b1.append(x1.next())

  // Print the resultant string that can be used in the POST Rest call
  s"""'{"schema": "${b1.toString().parseJson.compactPrint.replace("\"", "\\\"")}"}'"""

}