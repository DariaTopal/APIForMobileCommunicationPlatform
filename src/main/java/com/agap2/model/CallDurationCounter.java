package com.agap2.model;

public class CallDurationCounter {

	private int countNumberOfCallDurations;

	private Integer totalDuration;
	
	private double averageCallDuration;

	public CallDurationCounter() {
		super();
	}
	
	public double calculateAverageCallDuration() {
		
		int totalDurationInt = totalDuration.intValue();
		
		double totalDurationIntDouble = (double) totalDurationInt;
		double countNumberOfCallDurationsDouble = (double) countNumberOfCallDurations;
		
		averageCallDuration = totalDurationIntDouble/countNumberOfCallDurationsDouble;
		
		return averageCallDuration; 
	}

	public int getCountNumberOfCallDurations() {
		return countNumberOfCallDurations;
	}

	public void setCountNumberOfCallDurations(int countNumberOfCallDurations) {
		this.countNumberOfCallDurations = countNumberOfCallDurations;
	}

	public Integer getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(Integer totalDuration) {
		this.totalDuration = totalDuration;
	}

	public double getAverageCallDuration() {
		return averageCallDuration;
	}

	public void setAverageCallDuration(double averageCallDuration) {
		this.averageCallDuration = averageCallDuration;
	}
	
	
}
