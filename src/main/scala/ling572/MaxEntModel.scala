package ling572

import scala.collection._
import scala.collection.immutable
import util.Instance
import collection.immutable.HashMap

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

  lazy val classLabels = classLambdas.keys.toSeq.sorted
  
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

  def scoreInstance(line: Instance): (String, immutable.ListMap[String,Double] ) = {
    val scores = new mutable.HashMap[String,Double]()
    for ((label,lambdas) <- classLambdas) {
      val score = lambdas
        .filterKeys( k => line.hasFeature(k))
        .values
        .sum
      scores.put(label, math.exp(score))
    }
    val label = scores.maxBy(_._2)._1
    val normalizer = scores.values.sum
    val normedScores = immutable.ListMap() ++ scores.toList.sortBy(-_._2).map{ kv => (kv._1 , kv._2 / normalizer  )}
    (label,normedScores)
  }

}