package ling572;

import java.io.*;
import java.util.*;
import ling572.util.Instance;
import ling572.util.SVMLightReader;

public class Q4Driver {
	private File trainingData;
	private File outputFile;
	private File modelFile;
	

	public static void main(String[] args) {
		Q4Driver driver = new Q4Driver();
		driver.parseArgs(args);

		List<Instance> instances = SVMLightReader.indexInstances(driver.trainingData);
		
		ModelExpectation expectation = new ModelExpectation();
		expectation.setInstances(instances);
		
		if (driver.modelFile != null) {
			MaxEntModel maxEnt = new MaxEntModel();
			maxEnt.loadFromFile(driver.modelFile);
			expectation.setMaxEntModel(maxEnt);
		}
		
		expectation.build();
		expectation.generateOutput(driver.outputFile);
	}
	
	public void parseArgs(String[] args) {
		if (args.length < 2 || args.length > 3)
			exit("Error: usage Q4Driver training_data output_file [model_file]");
		
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
		
		if (args.length == 3) {
			try {
				this.modelFile = new File(args[2]);
			} catch(Exception e) {
				exit("Error: invalid model_file");
			}
		} else
			this.modelFile = null;
	}
	
	private static void exit(String errorText) {
		System.out.println(errorText);
		System.exit(1);
	}
}
