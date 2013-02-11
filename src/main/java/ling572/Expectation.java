package ling572;

import java.io.*;
import java.util.*;

import ling572.util.Frequency;
import ling572.util.Instance;

public abstract class Expectation {
	
	protected int instanceCount;
	protected Map<String, Frequency<String>> rawCounts = new HashMap<String, Frequency<String>>();
	private Map<String, Map<String, Double>> expectations = new HashMap<String, Map<String, Double>>();
	protected List<Instance> instances;
	
 	public void setInstances(List<Instance> instances) {
		this.instances = instances;
		this.instanceCount = instances.size();
		this.countFeatures(instances);
	}
 		
	private void countFeatures(List<Instance> instances) {
		for (Instance x : instances) {
			String y = x.getLabel();
			
			Frequency<String> classRawCounts = this.rawCounts.get(y);
			
			if (classRawCounts == null) {
				classRawCounts = new Frequency<String>();
				this.rawCounts.put(y, classRawCounts);
			}
			
			for (Map.Entry<String,Integer> entry : x.getFeatures().entrySet()) {
				String t = entry.getKey();
				classRawCounts.count(t);
			}
 		}
	}

	public List<String> getClasses () {
		List<String> classes = new ArrayList<String>(this.rawCounts.keySet());
		Collections.sort(classes);
		return classes;
	}
	
	public List<String> getFeaturesInClass(String classLabel) {
		List<String> features = new ArrayList<String>(this.rawCounts.get(classLabel).getCounts().keySet());
		Collections.sort(features);
		return features;
	}

	public void generateOutput(File output_file) throws IOException {
		try (ExpectationOutput output = new ExpectationOutput(output_file)) {
			for (String y : this.getClasses()) {	
				for (String t : this.getFeaturesInClass(y)) {
					double expectation = this.getExpectation(y, t);
					double count = this.getCount(y, t);
					output.printExpectations(y,t, expectation, count);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	protected double getExpectation(String y, String t) {
		Map<String, Double> classExp = this.expectations.get(y);
		
		double exp = 0d;
		
		try {
			exp = classExp.get(t);
		} catch(NullPointerException e) {
			//	ignore
		}
		
		return exp;
	}
	
	protected abstract double getCount(String y, String t);
	
	public abstract void build();
	
	protected void setExpectation(String y, String t, double exp) {
		Map<String, Double> classExp = this.expectations.get(y);
		
		if (classExp == null) {
			classExp = new HashMap<String, Double>();
			this.expectations.put(y, classExp);
		}
		
		classExp.put(t, exp);
	}
	
	protected abstract double calcExpectation(String y, String t);
}