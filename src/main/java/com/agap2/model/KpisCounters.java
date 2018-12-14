package com.agap2.model;

import java.util.HashMap;

public class KpisCounters {

	// Total number of processed JSON files
	private int numberOfProceesedFiles = 0;
	
	// Total number of rows
	private int numberOfProceesedRows = 0;
	
	// Total number of calls
	private int numberOfProceesedCalls = 0;
	
	// Total number of messages
	private int numberOfProceesedMessages = 0;
	
	// Total number of different origin country codes
	// (https://en.wikipedia.org/wiki/MSISDN)
	private int numberOfProceesedDifferentOriginCountryCodes = 0;
	
	// Total number of different destination country codes
	// (https://en.wikipedia.org/wiki/MSISDN)
	private int numberOfProceesedDifferentDestinationCountryCodes = 0;
	
	// Duration of each JSON process
	// where string is the date, double is spent time in milliseconds
	private HashMap<String, Long> processingTimes = new HashMap<String, Long>();

	public KpisCounters() {
		super();
	}

	public int getNumberOfProceesedFiles() {
		return numberOfProceesedFiles;
	}

	public int getNumberOfProceesedRows() {
		return numberOfProceesedRows;
	}

	public int getNumberOfProceesedCalls() {
		return numberOfProceesedCalls;
	}

	public int getNumberOfProceesedMessages() {
		return numberOfProceesedMessages;
	}

	public int getNumberOfProceesedDifferentOriginCountryCodes() {
		return numberOfProceesedDifferentOriginCountryCodes;
	}

	public int getNumberOfProceesedDifferentDestinationCountryCodes() {
		return numberOfProceesedDifferentDestinationCountryCodes;
	}

	public HashMap<String, Long> getProcessingTimes() {
		return processingTimes;
	}

	public void aggregateKpis(FileKpisCounter processedFileCounter) {
		
		this.numberOfProceesedFiles++;                            
		this.numberOfProceesedRows += processedFileCounter.getTotalNumberOfRows();                             
		this.numberOfProceesedCalls += processedFileCounter.getTotalNumberOfCalls(); 
		this.numberOfProceesedMessages += processedFileCounter.getTotalNumberOfMessages(); 
		this.numberOfProceesedDifferentOriginCountryCodes = processedFileCounter.getTotalNumberOfDifferentOriginCountryCodes();
		this.numberOfProceesedDifferentDestinationCountryCodes = processedFileCounter.getTotalNumberOfDifferentDestinationCountryCodes();
		
	}
}
