package ling572;

import ling572.util.Frequency;

public class EmpiricalExpectation extends Expectation {

	@Override
	protected double getCount(String y, String x) {
		Frequency<String> classCounts = this.rawCounts.get(y);
		return classCounts.get(x);
	}

	@Override
	public void build() {
		for (String y : this.getClasses()) {
			 for (String t : this.getFeaturesInClass(y)) {				 
				 double expectation = this.calcExpectation(y, t);
				 this.setExpectation(y, t, expectation);
			 }
		}
	}

	@Override
	protected double calcExpectation(String y, String t) {
		 Frequency<String> classRawCounts = this.rawCounts.get(y);
		 int rawCount = classRawCounts.get(t);
		 double expectation = (double)rawCount * 1/this.instanceCount;
		 return expectation;
	}
}