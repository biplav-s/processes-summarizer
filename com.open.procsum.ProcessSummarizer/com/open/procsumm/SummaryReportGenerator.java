/*
 * ******************************************************************
 * File:      SummaryReportGenerator.java
 * Purpose:   This file is to generate the summary report
 * Author:    Biplav Srivastava.
 * Created on Jan 5, 2010    
 * ******************************************************************
 */

package com.open.procsumm;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import com.open.procsumm.comparators.IProcessComparator;
import com.open.procsumm.model.ProcessModel;

public class SummaryReportGenerator {

	/**
	 * Write the summary file
	 * 
	 * @param fileName
	 * @param processes
	 * @param nearProcesses
	 * @param farProcesses
	 * @return
	 */
	public static boolean writeSummaryFile(String fileName,
			Vector<ProcessModel> processes,
			double thresholdUsed,
			ArrayList<Vector<String>> synNearProcesses,
			ArrayList<Vector<String>> synFarProcesses,
			ArrayList<Vector<String>> semNearProcesses,
			ArrayList<Vector<String>> semFarProcesses,
			IProcessComparator synComparator, IProcessComparator semComparator) {
		String tmpStr, tmpStr2;

		try {

			// -----------------------------------------------------------
			// -----------------------------------------------------------
			// Open the summary file
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					fileName));

			// Content now begins

			// Header and aggregate information
			tmpStr = "Summary generated at: " + new Date() + "\n";
			os.write(tmpStr.getBytes());

			// For top phrases, consider process names, steps and goals
			SegmentProfileProcessing spp = new SegmentProfileProcessing();
			for (int i = 0; i < processes.size(); i++) {
				tmpStr2 = processes.get(i).processName;
				spp.performProcessing(tmpStr2);
				if(processes.get(i).semPart.processGoals != null) {
				for (int j = 0; j < processes.get(i).semPart.processGoals.length; j++) {
					tmpStr2 = processes.get(i).semPart.processGoals[j];
					spp.performProcessing(tmpStr2);
				}
				}
//				for (int j = 0; j < processes.get(i).synPart.numProcessSteps; j++) {
//					tmpStr2 = processes.get(i).synPart.processStepNames[j];
//					spp.performProcessing(tmpStr2);
//				}
			}
			ArrayList<String> topPhrases = spp.getHighestKPhrases(5);
			tmpStr = "Top 5 phrases (non-steps) in repository = ";
			for (int i = 0; i < topPhrases.size(); i++) {
				if (i < topPhrases.size() - 1)
					tmpStr += "\n\t" + topPhrases.get(i) + ", ";
				else
					tmpStr += "\n\t" + topPhrases.get(i) + "\n";
			}
			os.write(tmpStr.getBytes());

			tmpStr = "\n---------- PROCESSES STATISTICS ------------\n";
			os.write(tmpStr.getBytes());

			tmpStr = "Number of processes in repository = " + processes.size()
					+ "\n";
			os.write(tmpStr.getBytes());

			// Processes statistics
			int min = 100000, max = 0, count = 0, tmpVal = 0;
			float avg;
			for (int i = 0; i < processes.size(); i++) {
				tmpVal = processes.get(i).synPart.numProcessSteps;
				if (tmpVal < min)
					min = tmpVal;
				if (tmpVal > max)
					max = tmpVal;
				count += tmpVal;
			}
			avg = (((float) count) / (float) processes.size());

			tmpStr = "Avg steps = " + avg + ", Range = [" + min + ", " + max
					+ "]\n";
			os.write(tmpStr.getBytes());

			// For top process steps, consider process step names only
			spp = new SegmentProfileProcessing();
			for (int i = 0; i < processes.size(); i++) {
				for (int j = 0; j < processes.get(i).synPart.numProcessSteps; j++) {
					tmpStr2 = processes.get(i).synPart.processStepNames[j];
					spp.performProcessing(tmpStr2);
				}
			}
			topPhrases = spp.getHighestKPhrases(5);
			tmpStr = "Top 5 step names in repository = ";
			for (int i = 0; i < topPhrases.size(); i++) {
				if (i < topPhrases.size() - 1)
					tmpStr += "\n\t" + topPhrases.get(i) + ", ";
				else
					tmpStr += "\n\t" + topPhrases.get(i) + "\n";
			}
			os.write(tmpStr.getBytes());

			tmpStr = "\n---------------------------------------------\n";
			os.write(tmpStr.getBytes());

			// Clusters

			tmpStr = "\n---------- PROCESS VARIANT ANALYSIS ------------\n";
			os.write(tmpStr.getBytes());
			tmpStr = "Threshold used = " + thresholdUsed + "\n";
			os.write(tmpStr.getBytes());


			// Find syntactic clusters
			// tmpStr = "Finding and reporting clusters by syntactic
			// comparison\n";
			// os.write(tmpStr.getBytes());

			findAndWriteCluster(os, fileName, synNearProcesses, synComparator);

			// Find semantic clusters
			// tmpStr = "Finding and reporting clusters by semantic
			// comparison\n";
			// os.write(tmpStr.getBytes());

			findAndWriteCluster(os, fileName, semNearProcesses, semComparator);

			// -----------------------------------------------------------
			// -----------------------------------------------------------
			// Close the summary file
			os.close();

		} catch (Exception e) {
			System.out.println("Exception: Could not write to file - "
					+ fileName + ". Exception is \n\t" + e);
			return false;
		}

		return true;
	}

	/**
	 * Write the summary file
	 * 
	 * @param fileName
	 * @param processes
	 * @param nearProcesses
	 * @param farProcesses
	 * @return
	 */
	public static boolean findAndWriteCluster(OutputStream os, String fileName,
			ArrayList<Vector<String>> nearProcesses,
			IProcessComparator comparator) {
		String tmpStr;

		try {

			// -----------------------------------------------------------
			// -----------------------------------------------------------
			tmpStr = "Finding and reporting clusters by comparator = "
					+ comparator.getProcessComaratorName() + "\n";
			os.write(tmpStr.getBytes());

			Vector<Vector<String>> clusterList = recursiveConsolidateMatchClusters(nearProcesses);

			tmpStr = "Number of clusters = " + clusterList.size() + "\n";
			os.write(tmpStr.getBytes());

			// Processes cluster statistics
			int min = 100000, max = 0, count = 0, tmpVal = 0;
			float avg;
			for (int i = 0; i < clusterList.size(); i++) {
				tmpVal = clusterList.get(i).size();
				if (tmpVal < min)
					min = tmpVal;
				if (tmpVal > max)
					max = tmpVal;
				count += tmpVal;
			}
			avg = (((float) count) / (float) clusterList.size());

			tmpStr = "Avg cluster size = " + avg + ", Range = [" + min + ", "
					+ max + "]\n\n";
			os.write(tmpStr.getBytes());

			// Print cluster details
			for (int i = 0; i < clusterList.size(); i++) {
				tmpStr = "Cluster " + i + " (Size=" + clusterList.get(i).size()
						+ "): " + clusterList.get(i).get(0) + "\n";
				os.write(tmpStr.getBytes());

				tmpStr = "\t -- ";
				for (int j = 1; j < clusterList.get(i).size(); j++) {
					if (j < clusterList.get(i).size() - 1)
						tmpStr += clusterList.get(i).get(j) + ",";
					else
						tmpStr += clusterList.get(i).get(j);
				}
				tmpStr += "\n";
				os.write(tmpStr.getBytes());
			}

			tmpStr = "---------------------------------------------------\n";
			os.write(tmpStr.getBytes());

		} catch (Exception e) {
			System.out.println("Exception: Could not write to file - "
					+ fileName + ". Exception is \n\t" + e);
			return false;
		}

		return true;
	}

	// ----------------------------------------- CLUSTERING
	// ------------------------------------

	public static ArrayList<Vector<String>> duplRawMatches;

	// New approach for clusters --
	// Call
	/**
	 * Consolidate raw matches into cluster
	 */
	public static Vector<Vector<String>> recursiveConsolidateMatchClusters(
			ArrayList<Vector<String>> myMatches) {

		Vector<Vector<String>> clusterList = new Vector<Vector<String>>();
		duplRawMatches = new ArrayList<Vector<String>>(myMatches);

		while (duplRawMatches != null && duplRawMatches.size() > 0) {
			String firstElement = duplRawMatches.get(0).get(0);
			Vector<String> aCluster = BuildACluster(firstElement);
			clusterList.add(aCluster);
		}

		return clusterList;

	}

	public static Vector<String> BuildACluster(String firstElement) {
		Vector<String> thisCluster = new Vector<String>();

		if (duplRawMatches.size() == 0)
			return thisCluster;

		// Get raw matches
		int i = 0;
		while (i < duplRawMatches.size()) {
			Vector<String> aPair = duplRawMatches.get(i);
			if (aPair.get(0).equalsIgnoreCase(firstElement)) {
				thisCluster.add(aPair.get(1));
				duplRawMatches.remove(i);
			} else if (aPair.get(1).equalsIgnoreCase(firstElement)) {
				thisCluster.add(aPair.get(0));
				duplRawMatches.remove(i);
			} else
				i++;
		}
		// Add the parent
		if (thisCluster.size() > 0)
			thisCluster.add(0, firstElement);

		// Now recursively check for the raw matches
		for (int j = 0; j < thisCluster.size(); j++) {
			Vector<String> subCluster = BuildACluster(thisCluster.get(j));

			// Add only if not present
			boolean duplicate = false;
			for (int m = 0; m < subCluster.size(); m++) {
				duplicate = false;
				for (int n = 0; n < thisCluster.size(); n++) {
					if (subCluster.get(m).equalsIgnoreCase(thisCluster.get(n))) {
						duplicate = true;
						break;
					}
				}
				if (!duplicate)
					thisCluster.add(subCluster.get(m));
			}
		}

		return thisCluster;
	}

	// ----------------------------------------- CLUSTERING
	// ------------------------------------

	/**
	 * This file is useful if one wants to generate the graphic cluster output
	 * 
	 * @param fileName
	 * @param myMatches
	 * @return
	 */
	public static boolean writeDotFile(String fileName,
			ArrayList<Vector<String>> myMatches) {
		String tmpStr;

		try {

			// -----------------------------------------------------------
			// Write the graph
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					fileName));
			// For undirected
			os.write("graph G {\n".getBytes());
			// For directed
			// os.write("digraph G {\n".getBytes());
			for (int i = 0; i < myMatches.size(); i++) {
				tmpStr = myMatches.get(i).get(0) + " -- "
						+ myMatches.get(i).get(1) + ";\n";
				// For directed
				// tmpStr = " " +
				// fileNamesSymbolTable.get(myMatches.get(i).get(0)) + " -> " +
				// fileNamesSymbolTable.get(myMatches.get(i).get(1)) + "; \n";
				// For directed, fullnames
				// tmpStr = " " + myMatches.get(i).get(0) + " -> " +
				// myMatches.get(i).get(1) + "; \n";
				os.write((tmpStr).getBytes());
			}

			os.write("}".getBytes());
			os.close();

		} catch (Exception e) {
			System.out.println("Exception: Could not write to file - "
					+ fileName + ". Exception is \n\t" + e);
			return false;
		}

		return true;
	}

}
