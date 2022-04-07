package com.open.procsumm.comparators;

import com.open.procsumm.model.ProcessModel;

public class BasicProcessStepComparator implements IProcessComparator {

	public String getProcessComaratorName() {
		return "ProcessesStepsCountComparator";
	}

	public double comparisonScore(ProcessModel firstProcess,
			ProcessModel secondProcess) {

		if (firstProcess == null || secondProcess == null)
			return IProcessComparator.INVALID_MATCH;

		double num = Math.abs(firstProcess.synPart.numProcessSteps
				- secondProcess.synPart.numProcessSteps); 
			
		double denom =	Math.max(firstProcess.synPart.numProcessSteps,
				secondProcess.synPart.numProcessSteps);
		
		double score = num / denom;
		
		return score;
	}
}
