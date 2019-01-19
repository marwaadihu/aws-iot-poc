package com.anil.iot.device.dto;

public class JobDocument {

	private String operation;

	private String firmwareUrl;

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return the firmwareUrl
	 */
	public String getFirmwareUrl() {
		return firmwareUrl;
	}

	/**
	 * @param firmwareUrl the firmwareUrl to set
	 */
	public void setFirmwareUrl(String firmwareUrl) {
		this.firmwareUrl = firmwareUrl;
	}

}
