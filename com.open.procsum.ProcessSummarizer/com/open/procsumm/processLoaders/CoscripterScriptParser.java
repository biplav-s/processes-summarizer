/*
 * ******************************************************************
 * File:      CoscripterScriptParser.java
 * Purpose:   Parse Coscripter scripts, all given in a dump
 * Author:    Biplav Srivastava.
 * Created on Jan 7, 2010    
 * ******************************************************************
 */

package com.open.procsumm.processLoaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import com.open.procsumm.model.ProcessModel;
import com.open.procsumm.model.ProcessPartSemantic;
import com.open.procsumm.model.ProcessPartSyntactic;

public class CoscripterScriptParser {

	public static final String scriptExtn = ".txt";
	HashMap<String, String> filesFound = new HashMap<String, String>();
	String[] plansFiles = null;
	
	// Let us read the scripts file and then we will populate the
	// process structure
	ArrayList<String> readSteps = new ArrayList<String>();



	public Vector<ProcessModel> parseDocument(String scriptsDump) {

		Vector<ProcessModel> processes = new Vector<ProcessModel>();
		ProcessModel tmpProcess = null;
		String tmpStr = null;

		// We will make ID and tmpID+count

		try {

			// Read the scripts dump
			BufferedReader input = new BufferedReader(new FileReader(scriptsDump));

				if (input == null) {
					System.out.println("Could not read scripts dump file = "
							+ scriptsDump);
					return processes;
				}

				String line = null;

				while ((line = input.readLine()) != null) {
					tmpStr = line.trim();

					if (tmpStr.length() == 0)
						continue;

					// Look for new test case or the end
					if (tmpStr.startsWith("\"")) {
						// Complete the old process read
						if(tmpProcess != null) {
							fillSyntacticPart(tmpProcess, readSteps);
							readSteps = new ArrayList<String>();
						}
							
						// New test case found; create object
						tmpProcess = createAndFillProcessHeaderAndSemanticsPart(tmpStr);
						// Now add the process to the vector
						processes.add(tmpProcess);
					}

					// Look for steps
					if (tmpStr.startsWith("*")) {
						tmpStr = tmpStr.trim();
						readSteps.add(tmpStr);
					}
					
					// Others are comments, etc; skip
				}

				// Complete the last process read
				if(tmpProcess != null) {
					fillSyntacticPart(tmpProcess, readSteps);
				}


		} catch (IOException e) {
			System.out.println("Exception : " + e);
		}

		return processes;
	}
	
	
	/**
	 * You are passed the coscripter header string
	 * @param headerString
	 * @return
	 */
	private ProcessModel createAndFillProcessHeaderAndSemanticsPart(String headerString) {
		
		String tmpStr = null;
		
		if(headerString == null)
			return null;
		
		// Creating the process structure
		ProcessModel tmpProcess = new ProcessModel();
		ArrayList<String> processFeatures = new ArrayList<String>();

		// Parse the headerString and put it in string and processFeatures
		StringTokenizer strTok = new StringTokenizer(headerString, ",");
		if(strTok == null)
			return null;
		
		while(strTok.hasMoreTokens()) {
			tmpStr = strTok.nextToken();
			
			if(tmpStr.startsWith("\"*")) {
				// Sometimes, steps are found in the header. Then pick them
				readSteps.add(tmpStr);
			} else
				// Capture as features
			processFeatures.add(tmpStr);
		}
		
		// Set the ID
		tmpProcess.processID = processFeatures.get(0);

		// Process name is the file name without extension
		tmpProcess.processName = processFeatures.get(1);
		System.out.println("Process is : ID - " + tmpProcess.processID
				+ ", name - " + tmpProcess.processName);

		
		
		// ---------------- Set semantic part --------------
		
		tmpProcess.semPart = new ProcessPartSemantic();

		// We will set the process goals as the names of the process
		// features
		// Get the processes' features

		int numFeatures = processFeatures.size();
		System.out.println("No. of features found : " + numFeatures);

		// Nothing to do if the number of features is 0. 
		// May want to set a default goal later on
		if (numFeatures > 2) {
			tmpProcess.semPart.processGoals = new String[numFeatures-2];
			for (int j = 2; j < processFeatures.size(); j++) {
				tmpProcess.semPart.processGoals[j-2] = processFeatures
						.get(j);
			}
		} // end-if numFeatures

		return tmpProcess;
	}
	
	/**
	 * You are passed the process structure and coscripter read steps 
	 * @param headerString
	 * @return
	 */
	private void fillSyntacticPart(ProcessModel tmpProcess, ArrayList<String> readSteps ) {
		// ---------------- Set syntactic part --------------
		tmpProcess.synPart = new ProcessPartSyntactic();

		int numSteps = readSteps.size();
		System.out.println("No. of steps found : " + numSteps);

		// Get its steps

		if (numSteps > 0) {
			tmpProcess.synPart.processStepNames = new String[numSteps];
			for (int j = 0; j < readSteps.size(); j++) {
				tmpProcess.synPart.processStepNames[j] = readSteps
						.get(j);
			}
		} // end-if numSteps
		tmpProcess.synPart.numProcessSteps = numSteps;
	}
	
}
