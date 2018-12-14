package com.agap2.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.agap2.model.Call;
import com.agap2.model.Communication;
import com.agap2.model.CommunicationType;
import com.agap2.model.FileKpisCounter;
import com.agap2.model.KpisCounters;
import com.agap2.model.Message;
import com.agap2.model.MetricCounter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class MobileCommunicationController {

	private static int countParseErrors;
	private static int countInvalidMessageType;
	private static int countNumberOfProcessedJsonFiles;
	private KpisCounters serviceKPIs = new KpisCounters();

	@RequestMapping(value = "/metrics", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public MetricCounter getCommunicationsForMetrics(@RequestBody List<CommunicationType> communications) {

		MetricCounter counter = new MetricCounter();

		Call call = null;
		Message message = null;
		Communication communication = new Communication();

		counter.setNumberOfRowsWithFieldsErrors(countParseErrors);
		counter.setNumberOfRowsWithInvalidMessageType(countInvalidMessageType);

		for (CommunicationType communicationType : communications) {

			if (communicationType instanceof Call) {
				call = (Call) communicationType;

				if (call.getOrigin() != null && call.getDestination() != null) {
					call.concatOriginDestinationCountryCode();
				}

				communication.getCommunications().add(call);

			} else if (communicationType instanceof Message) {
				message = (Message) communicationType;

				String messageContent = message.getMessageContent();

				if (messageContent != null && !messageContent.isEmpty()) {
					List<String> wordsOfContentMessage = message.splitMessageContent();
					counter.getWordsOfContentMessage().addAll(wordsOfContentMessage);
				}
				communication.getCommunications().add(message);
			}
		}

		counter.metricsCalculation(communication.getCommunications());

		return counter;

	}

	@RequestMapping(value = "/kpis", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public FileKpisCounter getCommunicationsForKPI(@RequestBody List<CommunicationType> communications) {

		Call call = null;
		Message message = null;
		Communication communication = new Communication();

		FileKpisCounter counter = new FileKpisCounter();

		counter.countTotalNumberOfRows(communications);
		counter.setTotalNumberOfProcessedJsonFiles(countNumberOfProcessedJsonFiles);

		for (CommunicationType communicationType : communications) {

			if (communicationType instanceof Call) {
				call = (Call) communicationType;
				if (call.getOrigin() != null) {
					call.retrieveOriginCountryCode();
				}
				if (call.getDestination() != null) {
					call.retrieveDestinationCountryCode();
				}
				communication.getCommunications().add(call);

			} else if (communicationType instanceof Message) {
				message = (Message) communicationType;
				if (message.getOrigin() != null) {
					message.retrieveOriginCountryCode();
				}
				if (message.getDestination() != null) {
					message.retrieveDestinationCountryCode();
				}
				communication.getCommunications().add(message);
			}
		}

		counter.kpisCalculation(communication.getCommunications());

		return counter;

	}

	private static final String BASE_URL = "https://raw.githubusercontent.com/vas-test/test1/master/logs/MCP_%s.json";

	@RequestMapping(value = "/metrics/{date}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public MetricCounter metricsByDate(@PathVariable String date)
			throws JsonParseException, JsonMappingException, IOException {
		
		// Create counter
		StopWatch watch = new StopWatch();
		watch.start(); 
		
		String fileURL = String.format(BASE_URL, date);
		
		// Read file
		List<CommunicationType> rows = readCommunicationsFromUrl(fileURL);
		
		// Read metrics
		MetricCounter metrics = this.getCommunicationsForMetrics(rows);
		
		// Read kpis
		FileKpisCounter currentFileKPIs = this.getCommunicationsForKPI(rows); 
		
		// Agregate kpis with kpis of service
		serviceKPIs.aggregateKpis(currentFileKPIs); 
		
		// Turn off the clock and count time 
		watch.stop();
		
		// Aggregates the elapsed duration aswell.
		serviceKPIs.getProcessingTimes().put(fileURL, watch.getTime()); 
		
		return metrics;
	}
	

	@RequestMapping(value = "/kpis", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public KpisCounters kpisByDate()
			throws JsonParseException, JsonMappingException, IOException {

		return this.serviceKPIs;
	}

	private static List<CommunicationType> readAllCommunications(BufferedReader rd) throws IOException {
		String line;
		ObjectMapper objectMapper = new ObjectMapper();
		List<CommunicationType> communications = new ArrayList<CommunicationType>();
		countParseErrors = 0;
		countInvalidMessageType = 0;
		while ((line = rd.readLine()) != null) {

			CommunicationType communication;
			try {
				communication = objectMapper.readValue(line, CommunicationType.class);

			} catch (com.fasterxml.jackson.core.JsonParseException e) {
				communication = null; // add as null when unable to parse json.
				countParseErrors = countParseErrors + 1;
			} catch (com.fasterxml.jackson.databind.exc.InvalidTypeIdException e) {
				communication = null; // add as null when unable to bind
				countInvalidMessageType = countInvalidMessageType + 1;
			}
			communications.add(communication);
		}

		return communications;
	}

	public static List<CommunicationType> readCommunicationsFromUrl(String url) throws IOException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			List<CommunicationType> communications = readAllCommunications(rd);
			
			return communications;
		} finally {
			is.close();
			countNumberOfProcessedJsonFiles++;
			
		}
		

	}

}
