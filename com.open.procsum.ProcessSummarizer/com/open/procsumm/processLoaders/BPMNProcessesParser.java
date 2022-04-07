package com.open.procsumm.processLoaders;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.open.procsumm.ProcessSummarizer;
import com.open.procsumm.model.ProcessModel;
import com.open.procsumm.model.ProcessPartSemantic;
import com.open.procsumm.model.ProcessPartSyntactic;

public class BPMNProcessesParser {

	HashMap<String, String> filesFound = new HashMap<String, String>();
	String[] bpmnFiles = null;

	
	/**
	 * removed the clean up
	 * @param files
	 * @return
	 */
	public Vector<ProcessModel> _parseDocument (String[] files){
		String processPath = "/ns0:definitions";
		String stepRelPath = "./ns0:process/ns0:task";
		// String processFeatureRelPath = "./ns1:processDiagram";
		String processFeatureRelPath = "./ns0:process/ns0:exclusiveGateway";

		String processPath2 = "/bpmn:definitions";
		String stepRelPath2 = "./bpmn:process/bpmn:task";
		// String processFeatureRelPath = "./ns1:processDiagram";
		String processFeatureRelPath2 = "./bpmn:collaboration/bpmn:pool";

		Vector<ProcessModel> processes = new Vector<ProcessModel>();
		ProcessModel tmpProcess = null;
		String tmpStr = null;

		// We will make ID and tmpID+count
		int count = 0;
		String tmpID = "ID";

		try {

			// Find all bpmns files in the given directory
//			this.bpmnFiles = identifyFilesToClean(bpmnsDir);
			// TODO (oma): we clean-up is required for the process services
			this.bpmnFiles = files;

			System.out.println("Number of bpmns to process = "
					+ this.bpmnFiles.length);

			for (int i = 0; i < this.bpmnFiles.length; i++) {

				String fileName = this.bpmnFiles[i];

				File xmlDocument = new File(fileName);

				SAXBuilder saxBuilder = new SAXBuilder(
						"org.apache.xerces.parsers.SAXParser");
				org.jdom.Document jdomDocument = saxBuilder.build(xmlDocument);

				java.util.List nodeList = XPath.selectNodes(jdomDocument,
						processPath);

				if (nodeList == null || nodeList.size() == 0) {
					nodeList = XPath.selectNodes(jdomDocument, processPath2);
					if (nodeList == null) {
						System.out.println("No process found under path -- '"
								+ processPath + "' or '" + processPath2 + "'");
						continue;
					}
				}

				// Iterate over the processes
				Iterator iter = nodeList.iterator();

				System.out.println("No. of process found : " + nodeList.size());
				while (iter.hasNext()) {

					// Now we have the handle to the process
					org.jdom.Element element = (org.jdom.Element) iter.next();

					tmpProcess = new ProcessModel();

					// Set the ID
					tmpProcess.processID = tmpID + count++;

					// Get its name
					tmpStr = element.getAttributeValue("id");
					tmpProcess.processName = tmpStr;
					System.out.println("Process is : ID - "
							+ tmpProcess.processID + ", name - "
							+ tmpProcess.processName);

					// ---------------- Set semantic part --------------
					tmpProcess.semPart = new ProcessPartSemantic();

					// We will set the process goals as the names of the process
					// features
					// Get the processes' features
					java.util.List processFeatures = XPath.selectNodes(element,
							processFeatureRelPath);

					// Try alternative if the first one does not work
					if (processFeatures == null || processFeatures.size() == 0)
						processFeatures = XPath.selectNodes(element,
								processFeatureRelPath2);

					int numFeatures = processFeatures.size();
					System.out
							.println("No. of features found : " + numFeatures);

					// Nothing to do if the number of features is 0. May want to
					// set a default goal later on
					if (numFeatures > 0) {

						tmpProcess.semPart.processGoals = new String[numFeatures];

						Iterator iter3 = processFeatures.iterator();

						int count3 = 0;
						while (iter3.hasNext()) {

							// Now we have the handle to the step
							org.jdom.Element element3 = (org.jdom.Element) iter3
									.next();

							tmpStr = element3
									.getAttributeValue("gatewayDirection");
							
							// To handle noise
							if (tmpStr == null) {
								tmpStr = "";
							}
							tmpProcess.semPart.processGoals[count3] = tmpStr;
							System.out.println("\t Goal is : " + tmpStr);

							count3++;

						}
					} // end-if numFeatures

					// ---------------- Set syntactic part --------------
					tmpProcess.synPart = new ProcessPartSyntactic();

					// Get its steps
					java.util.List stepList = XPath.selectNodes(element,
							stepRelPath);

					// Try alternative if the first one does not work
					if (stepList == null || stepList.size() == 0)
						processFeatures = XPath.selectNodes(element,
								stepRelPath2);

					int numSteps = stepList.size();
					System.out.println("No. of steps found : " + numSteps);
					tmpProcess.synPart.numProcessSteps = numSteps;

					// Nothing to do if the number of steps is 0
					if (numSteps > 0) {

						tmpProcess.synPart.processStepNames = new String[numSteps];

						Iterator iter2 = stepList.iterator();

						int count2 = 0;
						while (iter2.hasNext()) {

							// Now we have the handle to the step
							org.jdom.Element element2 = (org.jdom.Element) iter2
									.next();

							tmpStr = element2.getAttributeValue("name");

							// To handle noise
							if (tmpStr == null) {
								tmpStr = "";
							}
							tmpProcess.synPart.processStepNames[count2] = tmpStr;
							System.out.println("\t Step is : " + tmpStr);

							count2++;

						}

					} // end-if numSteps

					// Now add the process to the vector
					processes.add(tmpProcess);

				} // end processes iterator

				// XPath xpath = XPath
				// .newInstance("/catalog//journal:journal//article/@journal:level");
				// xpath.addNamespace("journal",
				// "http://www.w3.org/2001/XMLSchema-Instance");
				//
				// org.jdom.Attribute namespaceNode = (org.jdom.Attribute) xpath
				// .selectSingleNode(jdomDocument);
				// namespaceNode.setValue("Advanced");

				// OutputStream output = new FileOutputStream(new File(
				// "data/processes.xml"));
				// XMLOutputter outputter = new XMLOutputter();
				// outputter.output(jdomDocument, output);

			} // end-for processses in dir

		} catch (IOException e) {
			System.out.println("Exception : " + e);
		}

		catch (JDOMException e) {
			System.out.println("Exception : " + e);
		}

		return processes;
	}
	
	public Vector<ProcessModel> parseDocument(String bpmnsDir) {
		// Find all bpmns files in the given directory
		this.bpmnFiles = identifyFilesToClean(bpmnsDir);

		System.out.println("Number of bpmns to process = "
				+ this.bpmnFiles.length);

		return _parseDocument(this.bpmnFiles);

	}

	/**
	 * Identify the files to clean from a dir
	 * 
	 * @param inputDir
	 * @return
	 */
	public String[] identifyFilesToClean(String inputDir) {

		String[] bpmnFilesInDir = null;

		File dir = new File(inputDir);

		// It is also possible to filter the list of returned files.
		// This example returns all files that end with `_templates.doc'.
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.endsWith(".bpmn") || name.endsWith(".xml")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		};

		bpmnFilesInDir = dir.list(filter);

		for (int i = 0; i < bpmnFilesInDir.length; i++) {
			bpmnFilesInDir[i] = inputDir + "/" + bpmnFilesInDir[i];
			this.filesFound.put(bpmnFilesInDir[i].toLowerCase(),
					bpmnFilesInDir[i]);
		}

		return bpmnFilesInDir;
	}

	public static void main(String[] argv) {

		BPMNProcessesParser parser = new BPMNProcessesParser();
		parser.parseDocument("data/bpmns");
	}

}
