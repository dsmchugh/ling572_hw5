package ling572

import java.io.{PrintWriter, File}
import util.{ConditionalFreqDist, SVMLightReader}
import scala.collection.JavaConverters._
import scala.collection._

object Q2Driver extends App {

    override def main(args: Array[String]) {

      if (args.length != 3) {
        println("usage: Q2Driver [test_vectors] [model_file] [sys_out]")
        sys.exit(1)
      }

      // load model
    	val model = new MaxEntModel
    	scala.io.Source.fromFile(args(1)).getLines().foreach(model.parseLine)

      // parse test vectors
      val testVectors = SVMLightReader.indexInstances(new File(args(0))).asScala

      // helper to write sys file lines
      val sysOut = new PrintWriter(args(2))
      def writeSysOut(count: Int, label: String, features: immutable.ListMap[String, Double])  {
        sysOut.print("array:" + count + " " + label)
        features.foreach { kv =>
            sysOut.print(" " + kv._1 + " " + kv._2.toString)
        }
        sysOut.println()
      }

      // eval test vectors
      var count = 0
      var correct = 0.0
      val confusionMatrix = new ConditionalFreqDist[String]()
      testVectors.foreach { instance =>
        val (label, scores) = model.scoreInstance(instance)
        writeSysOut(count, label, scores)
        count += 1
        if (label == instance.getLabel) correct += 1
        confusionMatrix.add(instance.getLabel, label)
      }

      sysOut.close()

      // print confusion matrix
      val classLabels = confusionMatrix.keySet.toSeq.sorted

      print("\t\t")
      for (label <- classLabels) print(label + " ")
      println()
      for (gold <- classLabels) {
        print(gold + "\t")
        for (label <- classLabels) {
          print(confusionMatrix.N(gold, label) + "\t")
        }
        println()
      }

      println(" Test accuracy: " + (correct / count))
    }
}