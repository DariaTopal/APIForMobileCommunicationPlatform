package com.agap2.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

public class MetricCounter {

	private int numberOfMessagesWithBlankContent = 0;

	private int numberOfRowsWithFieldsErrors = 0;

	private int numberOfRowsWithInvalidMessageType = 0;

	private int numberOfRowsWithInvalidValues = 0;

	private Map<String, Integer> callsOriginDestinationGroupedByCountryCode = new HashMap<>();

	private Double relationshipBetweenOKKOCalls = null;

	private Map<String, CallDurationCounter> averageCallDurationGroupedByCountryCodeMap = new HashMap<>();

	private MultiValuedMap<Integer, Map.Entry<String, Integer>> wordOcurrenceRanking = new ArrayListValuedHashMap<>();

	private Map<String, Integer> wordOcurrence = new HashMap<>();

	private List<Map.Entry<String, Integer>> sortedWordOcurrenceMap = null;

	private Map<Integer, Collection<Entry<String, Integer>>> wordOcurrenceRankingMap = new HashMap<>();

	private List<String> wordsOfContentMessage = new ArrayList<String>();

	private List<Integer> allValuesFromWordOccurrenceMap = new ArrayList<Integer>();

	private int numberOfOKStatusCode = 0;

	private int numberOfKOStatusCode = 0;

	private String message = null;

	// Count rows with invalid values
	private void countRowsWithInvalidValues(CommunicationType communicationType) {

		if (communicationType instanceof Call) {
			Call call = (Call) communicationType;
			String status = call.getStatusCode();

			if (status == null || !(status.equals("OK") || status.equals("KO"))) {
				numberOfRowsWithInvalidValues++;
			}

		}

		if (communicationType instanceof Message) {

			Message message = (Message) communicationType;
			String messageStatus = message.getMessageStatus();

			if (messageStatus == null || !(messageStatus.equals("DELIVERED") || messageStatus.equals("SEEN"))) {
				numberOfRowsWithInvalidValues++;
			}
		}
	}

	// Count number of messages with blank content
	private void numberOfMessagesWithBlankContent(CommunicationType communicationType) {

		Message message = (Message) communicationType;
		String messageContent = message.getMessageContent();

		if (messageContent == null || messageContent.isEmpty()) {
			numberOfMessagesWithBlankContent++;
		}
	}

	private void countStatusCodeOKKO(Call call) {

		String statusCode = call.getStatusCode();

		if (statusCode != null) {
			if (statusCode.equals("OK")) {
				numberOfOKStatusCode++;
			}

			if (statusCode.equals("KO")) {
				numberOfKOStatusCode++;
			}
		}
	}

	//  Count relationship between OK/KO calls
	private void calculateRatioOKKO() {
		if (numberOfKOStatusCode == 0) {
			message = "Fail to calculate relationship between OK/KO because of division by 0 ";
		} else {

			double numberOfOK = (double) numberOfOKStatusCode;
			double numberOfKO = (double) numberOfKOStatusCode;
			relationshipBetweenOKKOCalls = numberOfOK / numberOfKO;
		}

	}

	// Count number of calls origin/destination grouped by country code
	private Map<String, Integer> countDistinctNumberOfCallsByCountryCode(String concatOriginDestinationCountryCode) {

		Integer actualValue = callsOriginDestinationGroupedByCountryCode.get(concatOriginDestinationCountryCode);

		if (actualValue != null) {
			actualValue++;
		} else {
			actualValue = 1;
		}

		callsOriginDestinationGroupedByCountryCode.put(concatOriginDestinationCountryCode, actualValue);

		return callsOriginDestinationGroupedByCountryCode;
	}

	private Map<String, Integer> countWordOccurrence() {

		for (String word : wordsOfContentMessage) {

			Integer actualValue = wordOcurrence.get(word);

			if (actualValue != null) {
				actualValue++;
			} else {
				actualValue = 1;
			}

			wordOcurrence.put(word, actualValue);
		}

		return wordOcurrence;
	}

	private void sortByValueWordOccurenceMap() {

		sortedWordOcurrenceMap = new ArrayList<Map.Entry<String, Integer>>(wordOcurrence.entrySet());

		Collections.sort(sortedWordOcurrenceMap, new Comparator<Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});

	}

	// Calculate word occurrence ranking for the given words in message_content
	// field.
	private void calculateWordOccurrenceRanking() {

		Integer actualRankingRanking = 1;

		Entry<String, Integer> actualValue = sortedWordOcurrenceMap.get(0);

		wordOcurrenceRanking.put(actualRankingRanking, actualValue);

		for (int i = 1; i < sortedWordOcurrenceMap.size(); i++) {

			Integer actualFrequencyValue = sortedWordOcurrenceMap.get(i).getValue();
			Integer previousFrequencyValue = sortedWordOcurrenceMap.get(i - 1).getValue();

			if (!actualFrequencyValue.equals(previousFrequencyValue)) {
				actualRankingRanking++;
			}

			actualValue = sortedWordOcurrenceMap.get(i);
			wordOcurrenceRanking.put(actualRankingRanking, actualValue);

		}
		wordOcurrenceRankingMap = wordOcurrenceRanking.asMap();
	}

	// Average call duration grouped by country code
	public Map<String, CallDurationCounter> countAverageDurationNumberOfCallsByCountryCode(Integer duration,
			String concatOriginDestinationCountryCode) {

		CallDurationCounter actualCallDurationCounter = averageCallDurationGroupedByCountryCodeMap
				.get(concatOriginDestinationCountryCode);

		if (actualCallDurationCounter != null) {

			int actualNumberOfCallDurations = actualCallDurationCounter.getCountNumberOfCallDurations();
			int newNumberOfCallDurations = actualNumberOfCallDurations + 1;
			actualCallDurationCounter.setCountNumberOfCallDurations(newNumberOfCallDurations);

			Integer actualTotalDuration = actualCallDurationCounter.getTotalDuration();
			actualCallDurationCounter.setTotalDuration(actualTotalDuration + duration);

			actualCallDurationCounter.calculateAverageCallDuration();

		} else {

			actualCallDurationCounter = new CallDurationCounter();
			int actualNumberOfCallDurations = 1;
			actualCallDurationCounter.setCountNumberOfCallDurations(actualNumberOfCallDurations);

			actualCallDurationCounter.setTotalDuration(duration);
			actualCallDurationCounter.calculateAverageCallDuration();
		}

		averageCallDurationGroupedByCountryCodeMap.put(concatOriginDestinationCountryCode, actualCallDurationCounter);

		return averageCallDurationGroupedByCountryCodeMap;
	}

	public void metricsCalculation(List<CommunicationType> communications) {

		countWordOccurrence();
		sortByValueWordOccurenceMap();
		if (sortedWordOcurrenceMap.size() != 0) {
			calculateWordOccurrenceRanking();
		}
		for (CommunicationType communicationType : communications) {

			countRowsWithInvalidValues(communicationType);

			if (communicationType instanceof Call) {
				Call call = (Call) communicationType;

				String concatOriginDestinationCountryCode = call.getConcatOriginDestinationCountryCode();

				countDistinctNumberOfCallsByCountryCode(concatOriginDestinationCountryCode);
				countStatusCodeOKKO(call);

				Integer callDuration = call.getDuration();

				if (callDuration != null) {
					countAverageDurationNumberOfCallsByCountryCode(callDuration,
							call.getConcatOriginDestinationCountryCode());
				}

			} else if (communicationType instanceof Message) {
				Message message = (Message) communicationType;

				numberOfMessagesWithBlankContent(message);
			}
		}

		calculateRatioOKKO();

	}

	public int getNumberOfRowsWithFieldsErrors() {
		return numberOfRowsWithFieldsErrors;
	}

	public void setNumberOfRowsWithFieldsErrors(int numberOfRowsWithFieldsErrors) {
		this.numberOfRowsWithFieldsErrors = numberOfRowsWithFieldsErrors;
	}

	public int getNumberOfOKStatusCode() {
		return numberOfOKStatusCode;
	}

	public void setNumberOfOKStatusCode(int numberOfOKStatusCode) {
		this.numberOfOKStatusCode = numberOfOKStatusCode;
	}

	public int getNumberOfKOStatusCode() {
		return numberOfKOStatusCode;
	}

	public void setNumberOfKOStatusCode(int numberOfKOStatusCode) {
		this.numberOfKOStatusCode = numberOfKOStatusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Double getRatioOKKO() {
		return relationshipBetweenOKKOCalls;
	}

	public void setRatioOKKO(Double ratioOKKO) {
		this.relationshipBetweenOKKOCalls = ratioOKKO;
	}

	public int getNumberOfRowsWithInvalidMessageType() {
		return numberOfRowsWithInvalidMessageType;
	}

	public void setNumberOfRowsWithInvalidMessageType(int numberOfRowsWithInvalidMessageType) {
		this.numberOfRowsWithInvalidMessageType = numberOfRowsWithInvalidMessageType;
	}

	public int getNumberOfRowsWithInvalidValues() {
		return numberOfRowsWithInvalidValues;
	}

	public void setNumberOfRowsWithInvalidValues(int numberOfRowsWithInvalidValues) {
		this.numberOfRowsWithInvalidValues = numberOfRowsWithInvalidValues;
	}

	public int getNumberOfMessagesWithBlankContent() {
		return numberOfMessagesWithBlankContent;
	}

	public void setNumberOfMessagesWithBlankContent(int numberOfMessagesWithBlankContent) {
		this.numberOfMessagesWithBlankContent = numberOfMessagesWithBlankContent;
	}

	public List<String> getWordsOfContentMessage() {
		return wordsOfContentMessage;
	}

	public Map<String, Integer> getWordOcurrence() {
		return wordOcurrence;
	}

	public void setWordOcurrence(Map<String, Integer> wordOcurrence) {
		this.wordOcurrence = wordOcurrence;
	}

	public Map<Integer, Collection<Entry<String, Integer>>> getWordOcurrenceRanking() {
		return wordOcurrenceRankingMap;
	}

	public void setWordOcurrenceRanking(Map<Integer, Collection<Entry<String, Integer>>> wordOcurrenceRankingMap) {
		this.wordOcurrenceRankingMap = wordOcurrenceRankingMap;
	}

	public Map<String, Integer> getCallsOriginDestinationGroupedByCountryCode() {
		return callsOriginDestinationGroupedByCountryCode;
	}

	public void setCallsOriginDestinationGroupedByCountryCode(
			Map<String, Integer> callsOriginDestinationGroupedByCountryCode) {
		this.callsOriginDestinationGroupedByCountryCode = callsOriginDestinationGroupedByCountryCode;
	}

	public Map<String, CallDurationCounter> getAverageCallDurationGroupedByCountryCodeMap() {
		return averageCallDurationGroupedByCountryCodeMap;
	}

	public void setAverageCallDurationGroupedByCountryCodeMap(
			Map<String, CallDurationCounter> averageCallDurationGroupedByCountryCodeMap) {
		this.averageCallDurationGroupedByCountryCodeMap = averageCallDurationGroupedByCountryCodeMap;
	}

	public List<Integer> getAllValuesFromWordOccurrenceMap() {
		return allValuesFromWordOccurrenceMap;
	}

	public void setAllValuesFromWordOccurrenceMap(List<Integer> allValuesFromWordOccurrenceMap) {
		this.allValuesFromWordOccurrenceMap = allValuesFromWordOccurrenceMap;
	}

}
