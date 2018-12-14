package com.agap2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Call extends CommunicationType {

	private Integer duration;
	private String statusCode;
	private String statusDescription;
	
	private String concatOriginDestinationCountryCode;
	
	public Call() {
		super(); 
	}
	

	@JsonProperty("duration")
	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@JsonProperty("status_code")
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	@JsonProperty("status_description")
	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public String getConcatOriginDestinationCountryCode() {
		return concatOriginDestinationCountryCode;
	}

	public void setConcatOriginDestinationCountryCode(String concatOriginDestinationCountryCode) {
		this.concatOriginDestinationCountryCode = concatOriginDestinationCountryCode;
	}

	public String concatOriginDestinationCountryCode() {
		
		String originCountryCode = retrieveOriginCountryCode();
		String destinationCountryCode = retrieveDestinationCountryCode();
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(originCountryCode);
		buffer.append("/");
		buffer.append(destinationCountryCode);
		setConcatOriginDestinationCountryCode(buffer.toString());
		return concatOriginDestinationCountryCode;
	}

}
