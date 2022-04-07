package com.open.procsumm.model;

public class ProcessModel {

	public String processID;
	public String processName;
	
	public ProcessPartSyntactic synPart;
	public ProcessPartSemantic semPart;
	
	// Give the content of the process
	public String giveContent() {
		String result = "";

		result = "Process ID = " + processID + "\n";
		result = "Process name = " + processName + "\n";
		
		String tmpStr = null;

		if(synPart != null) {
		tmpStr = synPart.giveContent();
		if (tmpStr != null)
			result += tmpStr;
		}
		if(semPart != null) {
		tmpStr = semPart.giveContent();
		if (tmpStr != null)
			result += tmpStr;
		}
		
		return result;
	}
}
