package com.open.procsumm.model;

public class ProcessPartSyntactic {

	public int numProcessSteps;
	public String[] processStepNames;
	
	// Give the content of the syntactic part
	public String giveContent() {
		String result = "";

		result = "No. of steps = " + numProcessSteps + "\n";
		
		if (processStepNames == null)
			return result;

		for (int i = 0; i < processStepNames.length; i++) {
			result += "Step " + i + " is '" + processStepNames[i] + "'\n";
		}

		return result;
	}
}
