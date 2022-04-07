package com.open.procsumm.model;

public class ProcessPartSemantic {
	public String[] processGoals;

	// Give the content of the semantic part
	public String giveContent() {
		String result = "";

		if (processGoals == null)
			return null;

		for (int i = 0; i < processGoals.length; i++) {
			result += "Goal " + i + " is '" + processGoals[i] + "'\n";
		}

		return result;
	}
}
