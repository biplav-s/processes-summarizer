package com.open.procsumm.comparators;

import com.open.procsumm.model.ProcessModel;

public interface IProcessComparator {
	
	public final double NO_MATCH = 1;
	public final double FULL_MATCH = 0;
	public final double INVALID_MATCH = -1; // represents invalid value
	
	// The name of the process comparator
	public String getProcessComaratorName();
	
	// A process comparator should return a score from 0..1
	public double comparisonScore(ProcessModel firstProcess, ProcessModel secondProcess);
}
