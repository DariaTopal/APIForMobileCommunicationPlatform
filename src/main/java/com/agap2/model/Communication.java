package com.agap2.model;

import java.util.ArrayList;
import java.util.List;



public class Communication {
	
	private List<CommunicationType> communications;
	  
	  

	public Communication() {
		super();
		communications = new ArrayList<CommunicationType>();
	}

	public List<CommunicationType> getCommunications() {
		return communications;
	}

	public void setCommunications(List<CommunicationType> communications) {
		this.communications = communications;
	}
	  
	  
}
