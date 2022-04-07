package com.open.procsumm;

import java.io.File;
import java.util.Vector;

import com.open.procsumm.model.ProcessModel;
import com.open.procsumm.model.ProcessPartSemantic;
import com.open.procsumm.model.ProcessPartSyntactic;
import com.open.procsumm.processLoaders.BPMNProcessesParser;
import com.open.procsumm.processLoaders.CoscripterScriptParser;
import com.open.procsumm.processLoaders.PDDLPlanParser;
import com.open.procsumm.processLoaders.SCProcessesParser;

public class LoadProcesses {

	public Vector<ProcessModel> loadProcesses(String processOption, String processesLocation) {

		if (processOption == null || processOption.length() == 0
				|| processOption.equalsIgnoreCase("DEFAULT")) {
			// Create dummy processes below
			// return createDummyProcessModels();
			return createDummyProcessModels();
		} else if (processOption.equalsIgnoreCase("SC")) {
			// Processes are in a single dir
			SCProcessesParser parser = new SCProcessesParser();
			// return parser.parseDocument(new File("data/isms/hitech-ism.xml"));
			// return parser.parseDocument(new File("data/isms/automotive-ism.xml"));
			// return parser.parseDocument(new File("data/isms/crossInd-mysap.xml"));
			if(processesLocation == null)
				processesLocation = "data/isms/pharma-ism.xml";
			return parser
					.parseDocument(new File(processesLocation));
			//				"C:/Biplav/Projects/SAP/Sedna2-ProcDriv/process/summary/pharma-ism.xml"));
			// return parser.parseDocument(new File("data/pharma-ism.xml"));
		} else if (processOption.equalsIgnoreCase("PD")) {
			// Processes are in files in a dir
			PDDLPlanParser parser = new PDDLPlanParser();
			return parser.parseDocument(processesLocation);
		} else if (processOption.equalsIgnoreCase("BP")) {
			// Processes are in files in a dir
			BPMNProcessesParser parser = new BPMNProcessesParser();
			return parser.parseDocument(processesLocation);
		} else if (processOption.equalsIgnoreCase("CO")) {
			// Processes are in files in a dir
			CoscripterScriptParser parser = new CoscripterScriptParser();
			return parser.parseDocument(processesLocation);
		}
		// Add others below

		else {
			// This is the default
			return createDummyProcessModels();
		}
	}

	public Vector<ProcessModel> createDummyProcessModels() {

		Vector<ProcessModel> processes = new Vector<ProcessModel>();
		ProcessModel tmpProcess = null;

		for (int i = 0; i < ProcessSummarizer.numProcesses; i++) {
			tmpProcess = new ProcessModel();
			tmpProcess.processID = (new Integer(i)).toString();
			tmpProcess.processName = "P" + i;

			// Set syntactic part
			tmpProcess.synPart = new ProcessPartSyntactic();
			tmpProcess.synPart.numProcessSteps = 1 + i * i;
			tmpProcess.synPart.processStepNames = new String[1 + i * i];
			for (int j = 0; j < 1 + i * i; j++) {
				tmpProcess.synPart.processStepNames[j] = "s" + i + j;
			}

			// Set semantic part
			tmpProcess.semPart = new ProcessPartSemantic();

			tmpProcess.semPart.processGoals = new String[1];

			// Do differently for odd and even processes
			if (i % 2 == 0)
				tmpProcess.semPart.processGoals[0] = "g0";
			else
				tmpProcess.semPart.processGoals[0] = "g1";

			// Add the created process
			processes.add(tmpProcess);
		}

		return processes;
	}
}
