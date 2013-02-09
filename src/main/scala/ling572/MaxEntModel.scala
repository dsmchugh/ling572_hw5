package ling572

import scala.collection._
import scala.collection.immutable

class MaxEntModel {

  var lambdasByClass = new mutable.HashMap[String,mutable.HashMap[String,Double]]()

  def addFeatureLambda(classLabel: String, featureLabel: String, lambda: Double) {
	  val featureMap = lambdasByClass.get(classLabel) match {
	    case Some(lambdas) => lambdas.put(featureLabel, lambda) 
	    case None => { 
	      val lambdas = new mutable.HashMap[String,Double]()
	      lambdas.put(featureLabel, lambda)
	      lambdasByClass.put(classLabel, lambdas)
	    }
	  }
  }

  // make immutable
  lazy val classLambdas: immutable.Map[String,immutable.Map[String,Double]] = {
     lambdasByClass.map(kv => (kv._1, kv._2.toMap)).toMap
  }
  
  val classLinePattern = """FEATURES FOR CLASS ([\S]+)""".r 
  val featureLinePattern = """[\s]+([\S]+)[\s]+([\S]+)""".r
  var currentClass:String = ""
  def parseLine(line: String) { 
	  line match {
	  	case classLinePattern(cl) => currentClass = cl
	  	case featureLinePattern(fn,lambda) => addFeatureLambda(currentClass, fn, lambda.toDouble)
	  	case _ => Nil
	  }	  
	}
}