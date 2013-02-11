package ling572;

import java.io.*;
import java.util.*;
import ling572.util.Instance;
import ling572.util.SVMLightReader;

public class Q3Driver {
	private File trainingData;
	private File outputFile;
	

	public static void main(String[] args) {
		Q3Driver driver = new Q3Driver();
		driver.parseArgs(args);
		
		List<Instance> instances = SVMLightReader.indexInstances(driver.trainingData);
		EmpiricalExpectation expectation = new EmpiricalExpectation();
		expectation.setInstances(instances);
		expectation.build();
		expectation.generateOutput(driver.outputFile);
	}
	
	public void parseArgs(String[] args) {
		if (args.length != 2)
			exit("Error: usage Q3Driver [training_data] [output_file]");
		
		try {
			this.trainingData = new File(args[0]);
		} catch (Exception e){
			exit("Error: invalid training_data file");
		}
		
		try {
			this.outputFile = new File(args[1]);
		} catch (Exception e){
			exit("Error: invalid output_file");
		}
	}
	
	private static void exit(String errorText) {
		System.out.println(errorText);
		System.exit(1);
	}
}
