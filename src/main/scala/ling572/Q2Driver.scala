package ling572

import java.io.File

object Q2Driver extends App {
	
	
    override def main(args: Array[String]) {
    	val model = new MaxEntModel
    	scala.io.Source.fromFile(args(0)).getLines.foreach(model.parseLine)
    	println(model.classLambdas)
    }
}