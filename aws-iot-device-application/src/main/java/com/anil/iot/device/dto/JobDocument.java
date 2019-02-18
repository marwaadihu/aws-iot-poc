package com.anil.iot.device.dto;

public class JobDocument {

	private String operation;

	private String firmwareUrl;

	private String privateKey;

	private String certificatePem;

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

	/**
	 * @return the privateKey
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * @param privateKey the privateKey to set
	 */
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * @return the certificatePem
	 */
	public String getCertificatePem() {
		return certificatePem;
	}

	/**
	 * @param certificatePem the certificatePem to set
	 */
	public void setCertificatePem(String certificatePem) {
		this.certificatePem = certificatePem;
	}

}
