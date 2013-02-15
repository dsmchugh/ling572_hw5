package ling572.port

import ling572.MaxEntModel
import collection.immutable.ListMap
import collection.JavaConverters._


class ModelExpectation extends Expectation {

  private var model:MaxEntModel = null
  private var defaultClassProb = 0.0

  override protected def getCount(y:String, x:String) = {
    val exp = this.getExpectation(y, x)
    exp * this.instanceCount
  }

  override def build() {
    val classes = this.getClasses

    this.defaultClassProb = 1d/this.getClasses.size

    for (x <- this.instances) {
      val classProbs: ListMap[String, Double] = if (model != null ) model.scoreInstance(x)._2  else null

      for (t <- x.getFeatures.asScala.keys) {
        for (y <- classes){
          val exp = if (this.model == null)
            this.calcExpectation(y, t)
          else {
            this.calcExpectation(y, t, classProbs.getOrElse(y,0.0))
          }

          this.addExp(y, t, exp)
        }
      }
    }

  }

  def setMaxEntModel(model:MaxEntModel) {
    this.model = model
  }


  override protected def calcExpectation(y:String, t:String ) = {
    this.calcExpectation(y, t, this.defaultClassProb)
  }

  protected def calcExpectation(y:String, t:String, classProb:Double) = {
    1d/this.instanceCount * classProb
  }

  private def addExp(y:String, x:String, exp:Double) {
    val totalExp = this.getExpectation(y, x)
    this.setExpectation(y, x, totalExp + exp)
  }

}
