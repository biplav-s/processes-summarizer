package com.open.procsumm.xml;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *  Use the TraX interface to perform a transformation in the simplest manner possible
 *  (3 statements).
 */
public class SimpleTransform
{
	public boolean doTransform(String transformFile, String inputFile, String outputFile) {
		
		try {
		  // Use the static TransformerFactory.newInstance() method to instantiate 
		  // a TransformerFactory. The javax.xml.transform.TransformerFactory 
		  // system property setting determines the actual class to instantiate --
		  // org.apache.xalan.transformer.TransformerImpl.
			TransformerFactory tFactory = TransformerFactory.newInstance();
			
			// Use the TransformerFactory to instantiate a Transformer that will work with  
			// the stylesheet you specify. This method call also processes the stylesheet
		  // into a compiled Templates object.
			Transformer transformer = tFactory.newTransformer(new StreamSource(transformFile));

			// Use the Transformer to apply the associated Templates object to an XML document
			// (foo.xml) and write the output to a file (foo.out).
			transformer.transform(new StreamSource(inputFile), new StreamResult(new FileOutputStream(outputFile)));
			
		} catch (Exception e) {
			System.out.println("Error:" + e);
			return false;
		}
		
		return true;
	}
	
	public static void main(String[] args)
    throws TransformerException, TransformerConfigurationException, 
           FileNotFoundException, IOException
  {  
		String baseDirNFile = "src/com/ibm/sedna2/htm/ExtractedContent";
		
		if(args.length >= 1) {
			baseDirNFile = args[0];
		}
		
		new SimpleTransform().doTransform(baseDirNFile + ".xsl", baseDirNFile + ".xml", baseDirNFile + ".htm");
		
 	
	System.out.println("************* The result is in " + baseDirNFile + ".htm *************");
  }
}
