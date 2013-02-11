package ling572;

import java.io.*;


public class ExpectationOutput implements AutoCloseable {
	private static final String SEPARATOR = " ";
	private BufferedWriter writer;
	
	public ExpectationOutput(String output_file) throws IOException {
		this(new File(output_file));
	}
	
	public ExpectationOutput(File output_file) throws IOException {
		this.writer = new BufferedWriter(new FileWriter(output_file));
	}
	
	public void printExpectations(String classLabel, String featureName, double expectation, double count) throws IOException {
		writer.write(classLabel);
		writer.write(SEPARATOR);
		writer.write(featureName);
		writer.write(SEPARATOR);
		writer.write(Double.toString(expectation));
		writer.write(SEPARATOR);
		writer.write(Double.toString(count));
		writer.newLine();
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}
}