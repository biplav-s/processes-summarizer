package com.open.procsumm.processLoaders;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.open.procsumm.ProcessSummarizer;
import com.open.procsumm.model.ProcessModel;
import com.open.procsumm.model.ProcessPartSemantic;
import com.open.procsumm.model.ProcessPartSyntactic;

public class SCProcessesParser {

	public Vector<ProcessModel> parseDocument(File xmlDocument) {

		String processPath = "/industry/scenario-group/scenario/process";
		String processPath2 = "/crossindustry/level0-item/level1-item/process";
		String stepRelPath = "variant/process-step";
		String processFeatureRelPath = "process-feature";
		String processFeatureRelPath2 = "sap-product";

		Vector<ProcessModel> processes = new Vector<ProcessModel>();
		ProcessModel tmpProcess = null;
		String tmpStr = null;

		// We will make ID and tmpID+count
		int count = 0;
		String tmpID = "ID";

		try {

			SAXBuilder saxBuilder = new SAXBuilder(
					"org.apache.xerces.parsers.SAXParser");
			org.jdom.Document jdomDocument = saxBuilder.build(xmlDocument);

			java.util.List nodeList = XPath.selectNodes(jdomDocument,
					processPath);

			if (nodeList == null || nodeList.size() == 0) {
				// Try second alternative
				nodeList = XPath.selectNodes(jdomDocument, processPath2);

				if (nodeList == null || nodeList.size() == 0) {
					System.out.println("No process found under path -- '"
							+ processPath + "'");
					return processes;
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
				tmpStr = element.getAttributeValue("name");
				tmpProcess.processName = tmpStr;
				System.out.println("Process is : ID - " + tmpProcess.processID
						+ ", name - " + tmpProcess.processName);

				// ---------------- Set semantic part --------------
				tmpProcess.semPart = new ProcessPartSemantic();

				// We will set the process goals as the names of the process
				// features
				// Get the processes' features
				java.util.List processFeatures = XPath.selectNodes(element,
						processFeatureRelPath);

				int numFeatures = processFeatures.size();
				
				//If features are not found, we will use the alternative
				if(numFeatures == 0) {
					processFeatures = XPath.selectNodes(element,
							processFeatureRelPath2);
						numFeatures = processFeatures.size();
				}
				
				
				System.out.println("No. of features found : " + numFeatures);

				// Nothing to do if the number of features is 0. May want to set
				// a default
				// goal later on
				if (numFeatures > 0) {

					tmpProcess.semPart.processGoals = new String[numFeatures];

					Iterator iter3 = processFeatures.iterator();

					int count3 = 0;
					while (iter3.hasNext()) {

						// Now we have the handle to the step
						org.jdom.Element element3 = (org.jdom.Element) iter3
								.next();

						tmpStr = element3.getAttributeValue("name");
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

		} catch (IOException e) {
			System.out.println("Exception : " + e);
		}

		catch (JDOMException e) {
			System.out.println("Exception : " + e);
		}

		return processes;
	}

	public static void main(String[] argv) {

		SCProcessesParser parser = new SCProcessesParser();
		parser.parseDocument(new File("data/isms/pharma-ism.xml"));
	}

}
