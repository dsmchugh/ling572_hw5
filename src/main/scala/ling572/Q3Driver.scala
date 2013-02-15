package ling572

import java.io.File
import ling572.util.SVMLightReader
import scala.collection.JavaConverters._


object Q3Driver extends App {

  var trainingData:File = null
  var outputFile:File = null

  def exit(errorText:String) {
    System.out.println(errorText)
    System.exit(1)
  }

  if (args.length != 2)
    exit("Error: usage Q3Driver [training_data] [output_file]")

  try {
    this.trainingData = new File(args(0))
  } catch {
    case e:Exception => exit("Error: invalid training_data file")
  }

  try {
    this.outputFile = new File(args(1))
  } catch {
    case e:Exception => exit("Error: invalid output_file")
  }

  val instances = SVMLightReader.indexInstances(trainingData).asScala.toList

  val expectation = new EmpiricalExpectation()
  expectation.setInstances(instances)

  expectation.build()
  expectation.generateOutput(outputFile)

}
