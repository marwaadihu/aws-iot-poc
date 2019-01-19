package com.anil.iot.device.dto;

public class JobExecution {

	private String clientToken;

	private long timestamp;

	private Execution execution;

	/**
	 * @return the clientToken
	 */
	public String getClientToken() {
		return clientToken;
	}

	/**
	 * @param clientToken the clientToken to set
	 */
	public void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the execution
	 */
	public Execution getExecution() {
		return execution;
	}

	/**
	 * @param execution the execution to set
	 */
	public void setExecution(Execution execution) {
		this.execution = execution;
	}

}
