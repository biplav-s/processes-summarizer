# processes-summarizer
This project implements ideas to summarize a collection of processes and cluster them for insights. The processes may be (AI) plans, business processes and scripts.

* See [paper](https://github.com/biplav-s/processes-summarizer/blob/main/com.open.procsum.ProcessSummarizer/doc/COMAD2010-PublishVersion-PS.pdf) - Biplav Srivastava, Processes Summarization, COMAD 2010: 179.
* See US Patent - [8682909](https://patents.justia.com/patent/8682909)
* Sample output is [here](https://github.com/biplav-s/processes-summarizer/tree/main/com.open.procsum.ProcessSummarizer/data/sampleoutput)


---
Main program to run is:
  ProcessSummarizer.java
  
Arguments:

// For business process
- BP data/bpmns STEPJACC FEATURESJACC

// For PDDL plans
- PD data/pddls/

// For co-scriptor scripts
- CO "data/scripts/procedures.txt" STEPJACC

// For SAP Industry processes
- SC data/isms/publicsec-ism.xml STEPJACC FEATURESJACC 0.2

//
Output:
- Summary output is in summary.txt
- Sample output is in data/sampleoutput
