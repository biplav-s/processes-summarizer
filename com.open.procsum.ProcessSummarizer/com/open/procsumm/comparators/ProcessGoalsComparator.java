package com.open.procsumm.comparators;

import com.open.procsumm.model.ProcessModel;

public class ProcessGoalsComparator implements IProcessComparator {


	public String getProcessComaratorName() {
		return "ProcessesGoalsJaccardComparator";
	}
	
	public double comparisonScore(ProcessModel firstProcess,
			ProcessModel secondProcess) {

		if (firstProcess == null || secondProcess == null)
			return IProcessComparator.INVALID_MATCH;

		// We will use Jaccard difference

		// We will determine how many goals of the first are
		// in the second and vice-versa, and then divide by the
		// total # of goals in both
		
		// If one of them has 0 steps but not the other, the difference is
		// maximum if the other is not 0
		if (firstProcess.semPart.processGoals == null
				|| secondProcess.semPart.processGoals == null) {
			if (firstProcess.semPart.processGoals == null
					&& secondProcess.semPart.processGoals == null)
				return 0; // Are similar
			else
				return 1; // Maximum distance
		}
		// If one of them has 0 goals but not the other, the difference is
		// maximum if the other is not 0
		if ((firstProcess.semPart.processGoals.length == 0 || secondProcess.semPart.processGoals.length == 0)) {
			if (firstProcess.semPart.processGoals.length == secondProcess.semPart.processGoals.length)
				return 0; // Are similar
			else
				return 1; // Maximum distance
		}
		
		
		int firstInSecond = 0;
		for (int i = 0; i < firstProcess.semPart.processGoals.length; i++) {
			for (int j = 0; j < secondProcess.semPart.processGoals.length; j++) {
				if (firstProcess.semPart.processGoals[i]
						.equalsIgnoreCase(secondProcess.semPart.processGoals[j])) {
					firstInSecond++;
				}
			}
		}
		int secondInFirst = 0;
		for (int i = 0; i < secondProcess.semPart.processGoals.length; i++) {
			for (int j = 0; j < firstProcess.semPart.processGoals.length; j++) {
				if (secondProcess.semPart.processGoals[i]
						.equalsIgnoreCase(firstProcess.semPart.processGoals[j])) {
					secondInFirst++;
				}
			}
		}

		double num = firstInSecond + secondInFirst;

		double denom = firstProcess.semPart.processGoals.length
				+ secondProcess.semPart.processGoals.length;

		double score = num / denom;

		return (1 - score);
	}
}
