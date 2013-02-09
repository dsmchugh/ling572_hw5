package ling572.util;

import java.io.*; 
import java.util.*;

public class Instance {

    private String label;
    private Map<String,Integer> features;

    public Instance(String label) {
        this.label = label;
        this.features = new HashMap<String,Integer>();
    }

    public void addFeature(String feature, int value) {
        this.features.put(feature, value);
    }

    public String getLabel() {
        return this.label;
    }

    public boolean hasFeature(String feature) {
        return this.features.containsKey(feature);
    }

    public Integer getFeatureValue(String feature) {
        return this.features.get(feature);
    }

    public Integer getFeatureValueOrDefault(String feature, int val) {
        if (this.hasFeature(feature))
            return this.features.get(feature);
        else
            return val;
    }

    public Map<String,Integer> getFeatures() {
        return this.features;
    }

    public void removeFeature(String feature) {
        this.features.remove(feature);
    }
    
	public static List<Instance> indexInstances(InputStream inputStream) throws IOException {
		List<Instance> instances = new ArrayList<Instance>();
	
		// line formatted as: label feature1:value1 feature2:value2 ..."
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] splitLine = line.split("\\s");
			
			String label = splitLine[0];
			Instance instance = new Instance(label);
			
			for (int i = 1; i < splitLine.length; i++) {
				String[] featureSplit = splitLine[i].split(":");
				String feature = featureSplit[0];
				
				Integer value = null;
				
				try {
					value = Integer.parseInt(featureSplit[1]);
				} catch (NumberFormatException e) {
					System.out.println("Error: cannot convert " + splitLine[i] + " value to integer.");
					continue;
				} catch (ArrayIndexOutOfBoundsException e) {
					//	ignore. extra space in file
					continue;
				}
				
				instance.addFeature(feature, value);
			}
			
			instances.add(instance);
		}
	
		reader.close();
		
		return instances;
	}
	
	public static List<Instance> indexInstances(File dataFile) throws IOException {
		InputStream inputStream = new FileInputStream(dataFile);
		
		return indexInstances(inputStream);
	}
}