package ling572;

import java.io.*;
import java.util.*;
import ling572.util.Instance;


public class Q3Driver {
	private File trainingData;
	private File outputFile;
	

	public static void main(String[] args) {
		Q3Driver driver = new Q3Driver();
		driver.parseArgs(args);
		
		try {
			List<Instance> instances = Instance.indexInstances(driver.trainingData);
			EmpiricalExpectations expectations = new EmpiricalExpectations();
			expectations.train(instances);
			expectations.generateOutput(driver.outputFile);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
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
