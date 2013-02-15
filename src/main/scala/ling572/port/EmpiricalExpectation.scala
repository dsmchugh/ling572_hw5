package ling572.port

class EmpiricalExpectation extends Expectation {

  override protected def getCount(y:String, x:String) = {
    this.rawCounts.get(y) match {
      case Some(count) => count.get(x)
      case None => 0.0
    }
  }

  override def build() {
    for (y <- this.getClasses) {
      for (t <- this.getFeaturesInClass(y)) {
        val expectation = this.calcExpectation(y, t)
        this.setExpectation(y, t, expectation)
      }
    }
  }

  override protected def calcExpectation(y:String, t:String):Double = {
    val rawCount:Double = this.rawCounts.get(y) match {
      case Some(classRawCounts) => classRawCounts.get(t).toDouble
      case None => 0.0
    }
    rawCount * 1/this.instanceCount
  }
}
