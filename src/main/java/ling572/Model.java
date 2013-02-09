package ling572;

import java.io.*;
import java.util.*;

import ling572.util.Frequency;
import ling572.util.Instance;


public class Model {
	private int instanceCount;
	private Map<String, Frequency<String>> rawCounts = new HashMap<String, Frequency<String>>();

	public void train(List<Instance> instances) {
		this.instanceCount = instances.size();
		
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

	public void empExpOutput(File output_file) throws IOException {
		List<String> classes = new ArrayList<String>(this.rawCounts.keySet());
		Collections.sort(classes);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(output_file));
		
		 for (String y : classes) {
			 Frequency<String> classRawCounts = this.rawCounts.get(y);
			 
			 for (Map.Entry<String, Integer> xEntry : classRawCounts.getCountsSortedByKey().entrySet()) {
				 String x = xEntry.getKey();
				 int count = xEntry.getValue();
				 
				 double expectation = this.calcEmpExp(count);
				 
				 writer.write(y + ' ');
				 writer.write(x + ' ');
				 writer.write(Double.toString(expectation) + ' ');
				 writer.write(Integer.toString(count));
				 writer.newLine();
			 }
		 }
		
		writer.close();
	}
	
	private double calcEmpExp(int featureCount) {
		return (double)featureCount * 1/this.instanceCount;
	}

}