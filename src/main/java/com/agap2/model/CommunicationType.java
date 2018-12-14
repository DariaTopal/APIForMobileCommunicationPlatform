package com.agap2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "message_type")
// defaultImpl = NoClass.class)
@JsonSubTypes({ @Type(value = Call.class, name = "CALL"), @Type(value = Message.class, name = "MSG") })
public abstract class CommunicationType {

//	private String messageType;

	private Long timestamp;

	private Long origin;

	private Long destination;

	private String originCountryCode;

	private String destinationCountryCode;

	public CommunicationType(String messageType, Long timestamp, Long origin, Long destination) {
		super();

		this.timestamp = timestamp;
		this.origin = origin;
		this.destination = destination;
	}

	public CommunicationType() {
		// TODO Auto-generated constructor stub
	}

	public String retrieveOriginCountryCode() {
		
		originCountryCode = getOrigin().toString().substring(0, 2);
		
		return originCountryCode;
	}

	public String retrieveDestinationCountryCode() {
		
		destinationCountryCode = getDestination().toString().substring(0, 2);
	  
		return destinationCountryCode;
	}

	@JsonProperty("timestamp")
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@JsonProperty("origin")
	public Long getOrigin() {
		return origin;
	}

	public void setOrigin(Long origin) {
		this.origin = origin;
	}

	@JsonProperty("destination")
	public Long getDestination() {
		return destination;
	}

	public void setDestination(Long destination) {
		this.destination = destination;
	}

	public String getOriginCountryCode() {
		return originCountryCode;
	}

	public void setOriginCountryCode(String originCountryCode) {
		this.originCountryCode = originCountryCode;
	}

	public String getDestinationCountryCode() {
		return destinationCountryCode;
	}

	public void setDestinationCountryCode(String destinationCountryCode) {
		this.destinationCountryCode = destinationCountryCode;
	}


	
}
