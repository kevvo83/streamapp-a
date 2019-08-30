package app1

import org.scalacheck._
import org.scalatest._

class TestSuite1 extends FlatSpec with RandomSensorData {

  behavior of "Random Sensor Data Generator"

  it should s"Generate ${defaultIters} elements when no num.samples Env Variable is set" in {
    val l: Int = (randomSensorData take (totalNumIters)).length
    assert(l === totalNumIters)
  }

}
