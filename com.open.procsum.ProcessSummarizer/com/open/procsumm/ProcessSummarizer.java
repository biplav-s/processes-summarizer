package com.open.procsumm;

import java.util.ArrayList;
import java.util.Vector;

import com.open.procsumm.comparators.BasicProcessStepComparator;
import com.open.procsumm.comparators.IProcessComparator;
import com.open.procsumm.comparators.ProcessGoalsComparator;
import com.open.procsumm.comparators.ProcessStepComparator;
import com.open.procsumm.model.ProcessModel;

public class ProcessSummarizer {

	public static int numProcesses = 6;
	public static double threshold = 0.5;
	public final static double INVALID_DISTANCE = -1;
	public static double distances[][] = null;

	public static ArrayList<Vector<String>> nearProcesses = null;
	public static ArrayList<Vector<String>> farProcesses = null;

	public static IProcessComparator synComparator = null;
	public static IProcessComparator semComparator = null;

	public static ArrayList<Vector<String>> synNearProcesses = null;
	public static ArrayList<Vector<String>> synFarProcesses = null;
	public static ArrayList<Vector<String>> semNearProcesses = null;
	public static ArrayList<Vector<String>> semFarProcesses = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Default processes are dummy ones.
		// Create dummy processes and do summary on them
		String processOption = "DEFAULT";
		String processesLocation = null;
		String synOption = null;
		String semOption = null;

		// User can specify which processes to work with
		System.out.println("-----------------------------------");
		System.out
				.println("USAGE: \n\t[<data-type>] [<processes-location>][<syn-comp-option>][<sem-comp-option>]");
		System.out.println("<data-type>= ''|'SC'|'PD'|'CO'|'BP'");
		System.out.println("<syn-option>= 'STEPCOUNT'|'STEPJACC'");
		System.out.println("<sem-option>= 'FEATURESJACC'");
		System.out.println("<threshold-option>= '<value>'");
		System.out.println("-----------------------------------");

		if (args != null && args.length > 0) {
			processOption = args[0];
			if (args.length > 1)
				processesLocation = args[1];
			if (args.length > 2)
				synOption = args[2];
			if (args.length > 3)
				semOption = args[3];
			if(args.length > 4) {
				// Automatically set the threshold
				threshold = Double.parseDouble(args[4]);
			}
		}

		// Some processesLocation with processOption = "SC"
		// "data/hitech-ism.xml", "data/automotive-ism.xml",
		// "C:/Biplav/Projects/SAP/Sedna2-ProcDriv/process/summary/pharma-ism.xml",
		// "data/pharma-ism.xml"

		// Load processes
		LoadProcesses lp = new LoadProcesses();
		Vector<ProcessModel> processes = lp.loadProcesses(processOption,
				processesLocation);

		// Now work with them
		doSummaryNComparison(processes, processOption, synOption, semOption);

		// Generate summary report
		SummaryReportGenerator.writeSummaryFile("summary.txt", processes,
				threshold,
				synNearProcesses, synFarProcesses, semNearProcesses,
				semFarProcesses, synComparator, semComparator);
	}

	/**
	 * @param args
	 */
	public static void doSummaryNComparison(Vector<ProcessModel> processes,
			String processOption, String synOption, String semOption) {

		if (processes == null) {
			System.out.println("INFO: no processes read.");
			return;
		}
		numProcesses = processes.size();

		BasicProcessStepComparator bpsc = new BasicProcessStepComparator();
		ProcessStepComparator psc = new ProcessStepComparator();
		ProcessGoalsComparator pgc = new ProcessGoalsComparator();

		// Initialize distance
		distances = new double[numProcesses][numProcesses];
		for (int i = 0; i < processes.size(); i++) {
			for (int j = 0; j < processes.size(); j++) {
				distances[i][j] = INVALID_DISTANCE;
			}
		}

		System.out.println("==========Processes being analyzed are =========");

		// Print the processes being analyzed
		for (int i = 0; i < processes.size(); i++) {
			System.out.println("Process " + i + " is \n"
					+ processes.get(i).giveContent());
		}

		System.out
				.println("==========Comparison follows  with steps comparison =========");
		nearProcesses = new ArrayList<Vector<String>>();
		farProcesses = new ArrayList<Vector<String>>();

		if (synOption == null || synOption.equalsIgnoreCase("STEPCOUNT"))
			synComparator = bpsc;
		else if (synOption.equalsIgnoreCase("STEPJACC"))
			synComparator = psc;

		// if (!processOption.equalsIgnoreCase("DEFAULT")) {
		// System.out.println("-----------------------------------------");
		// // System.out
		// // .println("=>By Significance model 2: By process steps and pairs
		// // <=");
		// synComparator = psc;

		System.out.println("=>Comparison by : "
				+ synComparator.getProcessComaratorName() + "<=");

		// Do comparison
		for (int i = 0; i < processes.size() - 1; i++) {
			for (int j = i + 1; j < processes.size(); j++) {

				distances[i][j] = synComparator.comparisonScore(processes
						.get(i), processes.get(j));

				// It need not be always symmetric
				distances[j][i] = distances[i][j];

				// System.out.println("Diff is = "
				// + psc.comparisonScore(processes[i], processes[j]));
			}
		}

		// Need to store interesting similarity and differences.
		findSimilarNDifferent(processes);

		// } else {
		// System.out.println("-----------------------------------------");
		// // System.out
		// // .println("=>By Significance model 1: By steps # and two clusters
		// // <=");
		//
		// BasicProcessStepComparator bpsc = new BasicProcessStepComparator();
		// synComparator = bpsc;
		// System.out.println("=>Comparison by : "
		// + synComparator.getProcessComaratorName() + "<=");
		//
		// for (int i = 0; i < processes.size() - 1; i++) {
		// for (int j = i + 1; j < processes.size(); j++) {
		//
		// distances[i][j] = synComparator.comparisonScore(processes
		// .get(i), processes.get(j));
		//
		// // It need not be always symmetric
		// distances[j][i] = distances[i][j];
		//
		// // System.out.println("Diff is = "
		// // + psc.comparisonScore(processes[i], processes[j]));
		// }
		// }
		//
		// // Need to store interesting similarity and differences.
		// findSimilarNDifferent(processes);
		//
		// }

		// ----------- Now printing ---------------
		for (int i = 0; i < nearProcesses.size(); i++) {
			System.out.println("Similar are : " + nearProcesses.get(i).get(0)
					+ "---" + nearProcesses.get(i).get(1));
		}
		for (int i = 0; i < farProcesses.size(); i++) {
			System.out.println("Dis-similar are : "
					+ farProcesses.get(i).get(0) + "-x-"
					+ farProcesses.get(i).get(1));
		}

		synNearProcesses = nearProcesses;
		synFarProcesses = farProcesses;

		// =============================================================
		// =============================================================
		// System.out
		// .println("==========Comparison follows with goals comparison
		// =========");

		if (semOption == null || semOption.equalsIgnoreCase("FEATURESJACC"))
			semComparator = pgc;

		// semComparator = pgc;
		System.out.println("=>Comparison by : "
				+ semComparator.getProcessComaratorName() + "<=");

		nearProcesses = new ArrayList<Vector<String>>();
		farProcesses = new ArrayList<Vector<String>>();

		// Do comparison
		for (int i = 0; i < processes.size() - 1; i++) {
			for (int j = i + 1; j < processes.size(); j++) {

				distances[i][j] = semComparator.comparisonScore(processes
						.get(i), processes.get(j));

				// It need not be always symmetric
				distances[j][i] = distances[i][j];

				// System.out.println("Diff is = "
				// + psc.comparisonScore(processes[i], processes[j]));
			}
		}

		// Need to store interesting similarity and differences.
		findSimilarNDifferent(processes);

		System.out.println("-----------------------------------------");
		// Output result
		// if (processOption.equalsIgnoreCase("DEFAULT")) {
		// System.out
		// .println("=>By Significance model 1: By goals and two clusters <=");
		// System.out.println("\t Similar are : ");
		// for (int i = 0; i < processes.size(); i++) {
		// if (processes.get(i).semPart.processGoals[0]
		// .equalsIgnoreCase("g0"))
		// System.out.println(" " + processes.get(i).processName);
		// }
		// System.out.println("\t Dis-similar are : ");
		// for (int i = 0; i < processes.size(); i++) {
		// if (processes.get(i).semPart.processGoals[0]
		// .equalsIgnoreCase("g1"))
		// System.out.println(" " + processes.get(i).processName);
		// }
		// } else {

		System.out.println("=>By Significance model 2: By goals and pairs <=");
		for (int i = 0; i < nearProcesses.size(); i++) {
			System.out.println("Similar are : " + nearProcesses.get(i).get(0)
					+ "---" + nearProcesses.get(i).get(1));
		}
		for (int i = 0; i < farProcesses.size(); i++) {
			System.out.println("Dis-similar are : "
					+ farProcesses.get(i).get(0) + "-x-"
					+ farProcesses.get(i).get(1));
		}

		// }

		semNearProcesses = nearProcesses;
		semFarProcesses = farProcesses;

		System.out.println("-----------------------------------------");

	}

	/**
	 * A central place to determine pair-wise similar and different processes
	 * 
	 * @param processes
	 * @param threshold
	 */
	public static void findSimilarNDifferent(Vector<ProcessModel> processes) {

		Vector<String> tmpPair = null;

		// Need to store interesting similarity and differences.
		for (int i = 0; i < processes.size() - 1; i++) {
			for (int j = i + 1; j < processes.size(); j++) {

				tmpPair = new Vector<String>();
				if (distances[i][j] >= 0.0 && distances[i][j] < threshold) {
					tmpPair.add("[" + processes.get(i).processID + "]"
							+ processes.get(i).processName);
					tmpPair.add("[" + processes.get(j).processID + "]"
							+ processes.get(j).processName);
					// nearProcesses.add("[" + processes.get(i).processID + "]"
					// +
					// processes.get(i).processName + "-"
					// + "[" + processes.get(j).processID + "]" +
					// processes.get(j).processName);
					nearProcesses.add(tmpPair);
				} else if (distances[i][j] >= threshold && distances[i][j] <= 1) {
					tmpPair.add("[" + processes.get(i).processID + "]"
							+ processes.get(i).processName);
					tmpPair.add("[" + processes.get(j).processID + "]"
							+ processes.get(j).processName);
					// farProcesses.add("[" + processes.get(i).processID + "]" +
					// processes.get(i).processName + "-"
					// + "[" + processes.get(j).processID + "]" +
					// processes.get(j).processName);
					farProcesses.add(tmpPair);

				}
			}
		}

	}

}
