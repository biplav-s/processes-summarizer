/*
 * ******************************************************************
 * File:      WriteExtractedContentToHTM.java
 * Purpose:   The Class will write extracted content to xml and run
 *            transforms to generate html
 * Author:    Biplav Srivastava.
 * Created on Jul 25, 2008    
 * ******************************************************************
 */

package com.open.procsumm.xml;


public class WriteExtractedContentToHTM {

	public static final String contents_TAG = "contents";
	public static final String extractedContent_TAG = "extractedContent";
	public static final String originModelReference_TAG = "originModelReference";
	public static final String modelReference_TAG = "modelReference";
	public static final String singleContent_TAG = "singleContent";
	public static final String contentList_TAG = "contentList";
	public static final String singleContentInList_TAG = "singleContentInList";
	public static final String repeatedContent_TAG = "repeatedContent";
	public static final String aRow_TAG = "aRow";
	public static final String aCol_TAG = "aCol";


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
