package ling572;

import java.util.*;

import ling572.util.Instance;

public class ModelExpectation extends Expectation {
	private MaxEntModel model = null;
	private double defaultClassProb = 0; 
	
	@Override
	protected double getCount(String y, String x) {
		double exp = this.getExpectation(y, x);
		double count = exp * this.instanceCount;
		return count;
	}

	@Override
	public void build() {
		List<String> classes = this.getClasses();
		
		this.defaultClassProb = 1d/this.getClasses().size();
		
		
		for (Instance x : this.instances) {
			Map<String, Double> classProbs = null;
			
			if (this.model != null)
				classProbs = this.model.scoreInstanceJava(x);
			
			for (String t : x.getFeatures().keySet()) {
				for (String y : classes){					
					double exp;
					
					if (this.model == null)
						exp = this.calcExpectation(y, t);
					else {
						double classProb = classProbs.get(y);
						exp = this.calcExpectation(y, t, classProb);
					}

					this.addExp(y, t, exp);
				}
			}
		}

	}
	
	public void setMaxEntModel(MaxEntModel model) {
		this.model = model;
	}

	@Override
	protected double calcExpectation(String y, String t) {
		return this.calcExpectation(y, t, this.defaultClassProb);
	}

	protected double calcExpectation(String y, String t, double classProb) {
		return 1d/this.instanceCount * classProb; 
	}
	
	private void addExp(String y, String x, double exp) {
		double totalExp = this.getExpectation(y, x);
		totalExp += exp;		
		this.setExpectation(y, x, totalExp);
	}
}
