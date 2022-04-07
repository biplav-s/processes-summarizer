package com.open.procsumm.comparators;

import com.open.procsumm.model.ProcessModel;

public class ProcessStepComparator implements IProcessComparator {

	public String getProcessComaratorName() {
		return "ProcessesStepsJaccardComparator";
	}
	
	public double comparisonScore(ProcessModel firstProcess,
			ProcessModel secondProcess) {

		if (firstProcess == null || secondProcess == null)
			return IProcessComparator.INVALID_MATCH;

		if (firstProcess.synPart == null || secondProcess.synPart == null)
			return IProcessComparator.INVALID_MATCH;

		
		// We will use Jaccard difference

		// We will determine how many steps of the first are
		// in the second and vice-versa, and then divide by the
		// total # of steps in both

		// If one of them has 0 steps but not the other, the difference is
		// maximum if the other is not 0
		if (firstProcess.synPart.processStepNames == null
				|| secondProcess.synPart.processStepNames == null) {
			if (firstProcess.synPart.processStepNames == null
					&& secondProcess.synPart.processStepNames == null)
				return 0; // Are similar
			else
				return 1; // Maximum distance
		}

		if (firstProcess.synPart.processStepNames.length == 0
				|| secondProcess.synPart.processStepNames.length == 0) {
			if (firstProcess.synPart.processStepNames.length == secondProcess.synPart.processStepNames.length)
				return 0; // Are similar
			else
				return 1; // Maximum distance
		}

		int firstInSecond = 0;
		for (int i = 0; i < firstProcess.synPart.processStepNames.length; i++) {
			for (int j = 0; j < secondProcess.synPart.processStepNames.length; j++) {
				if (firstProcess.synPart.processStepNames[i]
						.equalsIgnoreCase(secondProcess.synPart.processStepNames[j])) {
					firstInSecond++;
					break;
				}
			}
		}

		int secondInFirst = 0;
		for (int i = 0; i < secondProcess.synPart.processStepNames.length; i++) {
			for (int j = 0; j < firstProcess.synPart.processStepNames.length; j++) {
				if (secondProcess.synPart.processStepNames[i]
						.equalsIgnoreCase(firstProcess.synPart.processStepNames[j])) {
					secondInFirst++;
					break;
				}
			}
		}

		double num = firstInSecond + secondInFirst;

		double denom = firstProcess.synPart.numProcessSteps
				+ secondProcess.synPart.numProcessSteps;

		double score = num / denom;

		return (1 - score);
	}
}
