package com.agap2.model;

import java.util.ArrayList;
import java.util.List;

public class FileKpisCounter {

	private int totalNumberOfProcessedJsonFiles;

	private int totalNumberOfRows;

	private int totalNumberOfCalls;

	private int totalNumberOfMessages;

	private int totalNumberOfDifferentOriginCountryCodes;

	private int totalNumberOfDifferentDestinationCountryCodes;

	private List<String> originCountryCodes = new ArrayList<String>();

	private List<String> destinationCountryCodes = new ArrayList<String>();

	public FileKpisCounter() {
		super();
	}

	// Calculate total number of rows
	public void countTotalNumberOfRows(List<CommunicationType> communications) {
		if (communications != null && !communications.isEmpty()) {
			totalNumberOfRows = communications.size();
		}
	}

	// Count total number of different origin country codes
	private void countTotalNumberOfDifferentOriginCountryCodes(String originCountryCode) {
		if (!originCountryCodes.contains(originCountryCode)) {
			originCountryCodes.add(originCountryCode);
		}
	}

	// Count total number of different destination country codes
	private void countTotalNumberOfDifferentDestinationCountryCodes(String destinationCountryCode) {
		if (!destinationCountryCodes.contains(destinationCountryCode)) {
			destinationCountryCodes.add(destinationCountryCode);
		}
	}

	public void kpisCalculation(List<CommunicationType> communications) {
		
		for (CommunicationType communicationType : communications) {

			if (communicationType instanceof Call) {
				Call call = (Call) communicationType;
				String originCountryCodeCall = call.getOriginCountryCode();
				String destinationCountryCodeCall = call.getDestinationCountryCode();

				if (originCountryCodeCall != null) {

					countTotalNumberOfDifferentOriginCountryCodes(originCountryCodeCall);
				}

				if (destinationCountryCodeCall != null) {

					countTotalNumberOfDifferentDestinationCountryCodes(destinationCountryCodeCall);
				}
				// Calculate total number of calls
				totalNumberOfCalls++;

			} else if (communicationType instanceof Message) {
				Message message = (Message) communicationType;
				String originCountryCodeMessage = message.getOriginCountryCode();
				String destinationCountryCodeMessage = message.getDestinationCountryCode();

				if (originCountryCodeMessage != null) {

					countTotalNumberOfDifferentOriginCountryCodes(originCountryCodeMessage);
				}

				if (destinationCountryCodeMessage != null) {

					countTotalNumberOfDifferentDestinationCountryCodes(destinationCountryCodeMessage);
				}

				// Calculate total number of messages
				totalNumberOfMessages++;
			}
		}

		totalNumberOfDifferentOriginCountryCodes += originCountryCodes.size();
		totalNumberOfDifferentDestinationCountryCodes += destinationCountryCodes.size();
	}

	public int getTotalNumberOfRows() {
		return totalNumberOfRows;
	}

	public void setTotalNumberOfRows(int totalNumberOfRows) {
		this.totalNumberOfRows = totalNumberOfRows;
	}

	public int getTotalNumberOfCalls() {
		return totalNumberOfCalls;
	}

	public void setTotalNumberOfCalls(int totalNumberOfCalls) {
		this.totalNumberOfCalls = totalNumberOfCalls;
	}

	public int getTotalNumberOfMessages() {
		return totalNumberOfMessages;
	}

	public void setTotalNumberOfMessages(int totalNumberOfMessages) {
		this.totalNumberOfMessages = totalNumberOfMessages;
	}

	public int getTotalNumberOfDifferentOriginCountryCodes() {
		return totalNumberOfDifferentOriginCountryCodes;
	}

	public void setTotalNumberOfDifferentOriginCountryCodes(int totalNumberOfDifferentOriginCountryCodes) {
		this.totalNumberOfDifferentOriginCountryCodes = totalNumberOfDifferentOriginCountryCodes;
	}

	public int getTotalNumberOfDifferentDestinationCountryCodes() {
		return totalNumberOfDifferentDestinationCountryCodes;
	}

	public void setTotalNumberOfDifferentDestinationCountryCodes(int totalNumberOfDifferentDestinationCountryCodes) {
		this.totalNumberOfDifferentDestinationCountryCodes = totalNumberOfDifferentDestinationCountryCodes;
	}

	public int getTotalNumberOfProcessedJsonFiles() {
		return totalNumberOfProcessedJsonFiles;
	}

	public void setTotalNumberOfProcessedJsonFiles(int totalNumberOfProcessedJsonFiles) {
		this.totalNumberOfProcessedJsonFiles = totalNumberOfProcessedJsonFiles;
	}
	
}
