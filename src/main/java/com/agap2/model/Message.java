package com.agap2.model;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message extends CommunicationType {


	private String messageContent;
	
	private String messageStatus;
	
	public Message() {
		super(); 
	}
	
	public Message(String messageType, Long timestamp, Long origin, Long destination, String messageContent, String messageStatus) {
		super(messageType, timestamp, origin, destination);
		
		this.messageContent = messageContent;
		this.messageStatus = messageStatus;
	}
	
//	List<String> items = Arrays.asList(str.split("\\s*,\\s*"));
	
	public List<String> splitMessageContent() {
	
		List<String> items = Arrays.asList( messageContent.split(" "));
		
		return items;
	}

	@JsonProperty("message_content")
	public String getMessageContent() {
		return messageContent;
	}


	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	@JsonProperty("message_status")
	public String getMessageStatus() {
		return messageStatus;
	}


	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}

	
	
}
