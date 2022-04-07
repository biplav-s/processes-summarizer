/*
 * ******************************************************************
 * File:      ProcessesSummaryStructure
 * Purpose:   This file is to generate the summary report
 * Author:    Biplav Srivastava.
 * Created on Sep 7, 2010    
 * ******************************************************************
 */
package com.open.procsumm.model;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Vector;

import com.open.procsumm.xml.SimpleTransform;

public class ProcessesSummaryStructure {

	// Data members
	public String collectionName;
	public String dateCreated;
	public ArrayList<String> topPhrases; 

	public Vector<Vector<String>> semClusters;
	public Vector<Vector<String>> synlusters;

	// XML tags of data members
	public static final String summary_TAG = "summaryResult";
	public static final String statistics_TAG = "statisticsResult";
	public static final String aggregate_TAG = "aggregateResult";
	public static final String cluster_TAG = "clusterResult";
	public static final String aCluster_TAG = "aClusterResult";
	public static final String aValue_TAG = "aValue";

	public static final String clusterType_ATTRTAG = "clusterType"; // For clustering type: semantic, syntactic
	public static final String attributeName_ATTRTAG = "attributeName"; // Any attribute
	public static final String attributeValue_ATTRTAG = "attributeValue";

	public static final String modelReference_TAG = "modelReference";
	public static final String singleContent_TAG = "singleContent";
	public static final String contentList_TAG = "contentList";
	public static final String singleContentInList_TAG = "singleContentInList";
	public static final String repeatedContent_TAG = "repeatedContent";
	public static final String aRow_TAG = "aRow";
	public static final String aCol_TAG = "aCol";

	
	
	
	// Function to write it in XML
	public boolean writeExtractedContentInXML(String fileName) {

		
		BufferedWriter aBufferedWriter = null;
		try {
			aBufferedWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName)));
			aBufferedWriter.write("<?xml version=\"1.0\"?>\n");

			// Get the text to write
			String textToWrite = writeContentToString().toString();
			
			// Write it
			aBufferedWriter.write(textToWrite);

			aBufferedWriter.flush();
			aBufferedWriter.close();
		} catch (IOException anIOException) {
			System.out.println("Exception during writing file : " + fileName
					+ "\n" + anIOException);
			return false;
		}
		return true;
		
	}

	public StringBuffer writeContentToString() {

		StringBuffer sb = new StringBuffer();

		sb.append("<" + summary_TAG + ">\n");
		

		// Write tags and content.
		// sb.append("</" + aggregate_TAG + ">\n");
		
		
		sb.append("</" + summary_TAG + ">\n");

		return sb;
	}

	/**
	 * Write HTM from XML
	 * 
	 * @param xslInputFile
	 * @param xmlInputFile
	 * @param htmOutputFile
	 * @return
	 */
	public boolean transformXMLtoHTM(String xslInputFile, String xmlInputFile,
			String htmOutputFile) {

		try {
			SimpleTransform st = new SimpleTransform();
			st.doTransform(xslInputFile, xmlInputFile, htmOutputFile);
		} catch (Exception e) {
			System.out.println("Error: " + e);
			return false;
		}
		return true;
	}

	public static String encodeHTML(String s) {
		StringBuffer out = new StringBuffer();

		if (s == null)
			return out.append("<NULL>").toString();

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c > 127 || c == '"' || c == '<' || c == '>' || c == '&') {
				out.append("&#" + (int) c + ";");
			} else {
				out.append(c);
			}
		}
		return out.toString();
	}
}


