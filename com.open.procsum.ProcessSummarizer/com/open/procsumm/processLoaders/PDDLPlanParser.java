/*
 * ******************************************************************
 * File:      PDDLPlanParser.java
 * Purpose:   Parse PDDL plans
 * Author:    Biplav Srivastava.
 * Created on Jan 7, 2010    
 * ******************************************************************
 */

package com.open.procsumm.processLoaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.open.procsumm.model.ProcessModel;
import com.open.procsumm.model.ProcessPartSemantic;
import com.open.procsumm.model.ProcessPartSyntactic;

public class PDDLPlanParser {

	public static final String pddlPlanExtn = ".soln";
	HashMap<String, String> filesFound = new HashMap<String, String>();
	String[] plansFiles = null;

	public Vector<ProcessModel> parseDocument(String pddlPlansDir) {

		Vector<ProcessModel> processes = new Vector<ProcessModel>();
		ProcessModel tmpProcess = null;
		String tmpStr = null;

		// We will make ID and tmpID+count
		int count = 0;
		String tmpID = "ID";

		try {

			// Find all pddl plans files in the given directory
			this.plansFiles = identifyFilesToClean(pddlPlansDir);

			System.out.println("Number of plans to process = "
					+ this.plansFiles.length);

			for (int i = 0; i < this.plansFiles.length; i++) {

				BufferedReader input = new BufferedReader(new FileReader(
						this.plansFiles[i]));

				if (input == null) {
					System.out.println("Could not read plan file = "
							+ this.plansFiles[i]);
					return processes;
				}

				String line = null;

				// Creating the process structure
				tmpProcess = new ProcessModel();

				// Set the ID
				tmpProcess.processID = tmpID + count++;

				// Process name is the file name without extension
				tmpStr = this.plansFiles[i];
				tmpStr = tmpStr.substring(0, tmpStr
						.indexOf(PDDLPlanParser.pddlPlanExtn));
				tmpProcess.processName = tmpStr;
				System.out.println("Process is : ID - " + tmpProcess.processID
						+ ", name - " + tmpProcess.processName);

				// Let us read the plan file and then we will populate the
				// process structure
				ArrayList<String> processFeatures = new ArrayList<String>();
				ArrayList<String> readSteps = new ArrayList<String>();

				while ((line = input.readLine()) != null) {
					tmpStr = line.trim();

					if (tmpStr.length() == 0)
						continue;

					// Look for features
					if (tmpStr.startsWith(";")) {
						tmpStr = tmpStr.substring(1, tmpStr.length());
						tmpStr = tmpStr.trim();
						if (tmpStr.contains(" ")) {
							tmpStr = tmpStr.substring(0, tmpStr.indexOf(" "));
						}
						processFeatures.add(tmpStr);
					}

					// Look for steps
					if (tmpStr.contains("(")) {
						tmpStr = tmpStr.substring(tmpStr.indexOf("(")+1, tmpStr
								.length());
						tmpStr = tmpStr.trim();
						if (tmpStr.contains(" ")) {
							tmpStr = tmpStr.substring(0, tmpStr.indexOf(" "));
						}
						readSteps.add(tmpStr);
					}
				}

				// ---------------- Set semantic part --------------
				tmpProcess.semPart = new ProcessPartSemantic();

				// We will set the process goals as the names of the process
				// features
				// Get the processes' features

				int numFeatures = processFeatures.size();
				System.out.println("No. of features found : " + numFeatures);

				// Nothing to do if the number of features is 0. May want to set
				// a default
				// goal later on
				if (numFeatures > 0) {
					tmpProcess.semPart.processGoals = new String[numFeatures];
					for (int j = 0; j < processFeatures.size(); j++) {
						tmpProcess.semPart.processGoals[j] = processFeatures
								.get(j);
					}
				} // end-if numFeatures

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

				// Now add the process to the vector
				processes.add(tmpProcess);

			} // end processes iterator

		} catch (IOException e) {
			System.out.println("Exception : " + e);
		}

		return processes;
	}

	/**
	 * Identify the files to clean from a dir
	 * 
	 * @param inputDir
	 * @return
	 */
	public String[] identifyFilesToClean(String inputDir) {

		String[] pddlPlanFilesInDir = null;

		File dir = new File(inputDir);

		// It is also possible to filter the list of returned files.
		// This example returns all files that end with `_templates.doc'.
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.endsWith(".soln") || name.endsWith(".pddl")
						|| name.endsWith(".SOLN") || name.endsWith(".PDDL")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		};

		pddlPlanFilesInDir = dir.list(filter);

		for (int i = 0; i < pddlPlanFilesInDir.length; i++) {
			pddlPlanFilesInDir[i] = inputDir +  pddlPlanFilesInDir[i];
			this.filesFound.put(pddlPlanFilesInDir[i].toLowerCase(),
					pddlPlanFilesInDir[i]);
		}

		return pddlPlanFilesInDir;
	}

	/**
	 * Test driver
	 * 
	 * @param argv
	 */
	public static void main(String[] argv) {

		PDDLPlanParser parser = new PDDLPlanParser();
		parser.parseDocument("data/pddls/");
	}
}
