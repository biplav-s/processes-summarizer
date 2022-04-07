package com.open.procsumm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class SegmentProfileProcessing {

	public final static int PHRASE_LENGTH_CUTOFF = 200;
	private TreeMap<String, Integer> repeatCounts = null;
	private HashMap<String, Integer> topKStringsNCounts = null;

	public SegmentProfileProcessing() {
		repeatCounts = new TreeMap<String, Integer>();
		topKStringsNCounts = new HashMap<String, Integer>();
	}

	// External applications see this
	public boolean performProcessing(String inputText, HashMap<String, String> stopList) {

		if(stopList == null) {
			// Do without stoplist
			return performProcessing(inputText);
		}
		
		String rawText = inputText;
		if (repeatCounts == null || rawText == null || rawText.length() == 0) {
			return false;
		}
		
		rawText = rawText.trim();
		if(rawText.equalsIgnoreCase(""))
			return false;

		// We will not consider phrases longer than PHRASE_LENGTH_CUTOFF
		if (rawText.length() > PHRASE_LENGTH_CUTOFF)
			rawText = rawText.substring(0, PHRASE_LENGTH_CUTOFF);
		
		// The marker should not be in the stop list
		if(stopList.containsKey(rawText))
				return false;

		Integer frequency = (Integer) repeatCounts.get(rawText);
		if (frequency == null) {
			frequency = new Integer(1);
		} else {
			int value = frequency.intValue();
			frequency = new Integer(value + 1);
		}
		repeatCounts.put(rawText, frequency);

		return true;

	}

	// External applications see this
	public boolean performProcessing(String inputText) {

		String rawText = inputText;
		if (repeatCounts == null || rawText == null || rawText.length() == 0) {
			return false;
		}
		
		rawText = rawText.trim();
		if(rawText.equalsIgnoreCase(""))
			return false;

		// We will not consider phrases longer than PHRASE_LENGTH_CUTOFF
		if (rawText.length() > PHRASE_LENGTH_CUTOFF)
			rawText = rawText.substring(0, PHRASE_LENGTH_CUTOFF);

		Integer frequency = (Integer) repeatCounts.get(rawText);
		if (frequency == null) {
			frequency = new Integer(1);
		} else {
			int value = frequency.intValue();
			frequency = new Integer(value + 1);
		}
		repeatCounts.put(rawText, frequency);

		return true;

	}

	public ArrayList<String> getHighestKPhrases(int k) {
		ArrayList<String> topStrings = new ArrayList<String>();

		// ---- Quickly do sorting on the frequencies

		// Get the list of all frequencies
		Collection c = repeatCounts.values();
		
  	    Iterator iter = c.iterator();
  	    
  	    ArrayList<Integer> v = new ArrayList<Integer>();
		while(iter.hasNext()) {
			v.add((Integer) iter.next());
		}
		
		// Now do the simple bubble-sort in descending order
		Integer tmpInt;
		for(int i=0; i<v.size()-1; i++) {
			for(int j=i+1; j<v.size(); j++) {
				if(v.get(i) < v.get(j)) {
					tmpInt = v.get(i);
					v.set(i, v.get(j));
					v.set(j, tmpInt);
					}
			}
		}
		


		// Now find the Strings with the highest frequencies
		int numStrings = 0;
		for (int i = 0; i < v.size(); i++) {
			Integer freq = v.get(i);
			ArrayList<String> tmpList = getKeysOfValue(freq);
			if (tmpList != null) {
				for (int j = 0; j < tmpList.size(); j++) {
					// Only need to do till k
					if(numStrings >= k)
						break;	
					topStrings.add(tmpList.get(j));
					topKStringsNCounts.put(tmpList.get(j), freq);
					numStrings++;
				}
			}
		}

		return topStrings;
	}

	public ArrayList<String> getKeysOfValue(Integer freq) {

		ArrayList<String> matchingStrings = new ArrayList<String>();

		Iterator keyValuePairs1 = repeatCounts.entrySet().iterator();
		for (int i = 0; i < repeatCounts.size(); i++) {
			Map.Entry entry = (Map.Entry) keyValuePairs1.next();
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();

			if (freq == value) {
				matchingStrings.add(key);
			}

		}

		return matchingStrings;
	}
	
	
	public HashMap<String, Integer> getTopKStringsWithCount() {
		return this.topKStringsNCounts;
	}
	
	public TreeMap<String, Integer> getTopRepeatStringsWithCount() {
		return this.repeatCounts;
	}

}
