package com.anil.iot.device.listener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.anil.iot.device.controller.DeviceController;
import com.anil.iot.device.dto.JobDocument;
import com.anil.iot.device.dto.JobExecution;
import com.google.gson.Gson;

public class StartNextJobListener extends AWSIotTopic {

	private DeviceController deviceController;

	private static final Logger LOGGER = LoggerFactory.getLogger(StartNextJobListener.class);

	@Autowired
	public StartNextJobListener(String topic, DeviceController deviceController) {
		super(topic);
		this.deviceController = deviceController;
	}

	@Override
	public void onMessage(AWSIotMessage message) {
		LOGGER.info("message received: {}", message.getStringPayload());
		JobExecution jobExecution = new Gson().fromJson(message.getStringPayload(), JobExecution.class);

		String operation = jobExecution.getExecution().getJobDocument().getOperation();
		try {
			switch (operation.toLowerCase()) {
			case "change_certificate":
				changeCertificate(jobExecution.getExecution().getJobDocument());

				break;
			case "firmware update operation":
				String firmwareUrl = jobExecution.getExecution().getJobDocument().getFirmwareUrl();
				downloadFromUrl(firmwareUrl, "anil-poc.zip");
				break;
			default:
				break;
			}
			deviceController.updateJob(jobExecution.getExecution().getJobId());
		} catch (IOException | AWSIotException e) {
			LOGGER.error("exception: {}", e);
		}
	}

	private void changeCertificate(JobDocument jobDocument) throws IOException {
		String certificatePem = jobDocument.getCertificatePem();
		LOGGER.info("changeCertificate method called");

		Files.write(Paths.get("/home/anil/anil/training/aws-iot/device_cert/server.crt"), certificatePem.getBytes());

	}

	private void downloadFromUrl(String downloadUrl, String outputFileName) throws IOException {
		URL url = new URL(downloadUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		InputStream in = connection.getInputStream();
		FileOutputStream out = new FileOutputStream(outputFileName);
		copy(in, out, 1024);
		out.close();
	}

	private void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
		byte[] buf = new byte[bufferSize];
		int n = input.read(buf);
		while (n >= 0) {
			output.write(buf, 0, n);
			n = input.read(buf);
		}
		output.flush();
	}

}