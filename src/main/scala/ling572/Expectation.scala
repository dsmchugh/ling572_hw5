package ling572

import ling572.util.{Frequency, Instance}

import scala.collection._
import java.io._
import scala.collection.JavaConverters._
import scala.Some

abstract class Expectation {

  protected var instances: List[Instance] = null
  protected var instanceCount: Int  = 0
  protected val rawCounts = new mutable.HashMap[String,Frequency[String]]()
  private val expectations = new mutable.HashMap[String,mutable.HashMap[String,Double]]()

  def build()

  protected def calcExpectation(y:String, t:String): Double

  protected def getCount(y:String, t:String): Double

  def setInstances(instances: List[Instance]) {
    this.instances = instances
    this.instanceCount = instances.size
    this.countFeatures(instances)
  }

  private def countFeatures(instances: List[Instance]) {
    for ( x:Instance <- instances) {
      val y = x.getLabel
      val classRawCounts = rawCounts.getOrElseUpdate(y, new Frequency[String])
      for (t <- x.getFeatures.asScala.keys) {
        classRawCounts.count(t)
      }
    }
  }

  def getClasses:Array[String] = {
    this.rawCounts.keySet.toSeq.sorted.toArray
  }

  def getFeaturesInClass(classLabel:String):Array[String] = {
    this.rawCounts.get(classLabel) match {
      case Some(frequency) => frequency.getCounts.keySet.asScala.toSeq.sorted.toArray
      case None => new Array[String](0)
    }
  }

  def generateOutput(output_file: File) {
    val SEPARATOR = " "

    try {
      val writer = new BufferedWriter(new FileWriter(output_file))
      for (y <- this.getClasses) {
        for (t <- this.getFeaturesInClass(y)) {
          val expectation = this.getExpectation(y, t)
          val count = this.getCount(y, t)

          val stringCount = if (math.floor(count) == count) count.toInt.toString else count.toString

          writer.write(y)
          writer.write(SEPARATOR)
          writer.write(t)
          writer.write(SEPARATOR)
          writer.write(expectation.toString)
          writer.write(SEPARATOR)
          writer.write(stringCount)
          writer.newLine()
        }
      }
      writer.close()
    } catch {
      case e:IOException => {
        e.printStackTrace()
        System.exit(1)
      }
    }
  }

  protected def getExpectation(y:String, t:String):Double = {
    this.expectations.get(y) match {
      case Some(exp) => exp.getOrElse(t,0.0)
      case None => 0.0
    }
  }

  protected def setExpectation(y:String, t:String, exp:Double) {
    val classExp = this.expectations.getOrElseUpdate(y,new mutable.HashMap[String,Double]())
    classExp.put(t, exp)
  }

}
