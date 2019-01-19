package com.anil.iot.device.dto;

public class Execution {

	private String jobId;

	private String status;

	private JobDocument jobDocument;

	/**
	 * @return the jobId
	 */
	public String getJobId() {
		return jobId;
	}

	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the jobDocument
	 */
	public JobDocument getJobDocument() {
		return jobDocument;
	}

	/**
	 * @param jobDocument the jobDocument to set
	 */
	public void setJobDocument(JobDocument jobDocument) {
		this.jobDocument = jobDocument;
	}

}
