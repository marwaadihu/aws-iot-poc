package com.anil.iot.device.listener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.anil.iot.device.controller.DeviceController;
import com.anil.iot.device.dto.JobExecution;
import com.google.gson.Gson;

@Service
public class StartNextJobListener extends AWSIotTopic {

	@Autowired
	private DeviceController deviceController;

	private static final Logger LOGGER = LoggerFactory.getLogger(StartNextJobListener.class);

	public StartNextJobListener(String topic) {
		super(topic);
	}

	@Override
	public void onMessage(AWSIotMessage message) {
		LOGGER.info("message received: {}", message.getStringPayload());
		JobExecution jobExecution = new Gson().fromJson(message.getStringPayload(), JobExecution.class);

		String firmwareUrl = jobExecution.getExecution().getJobDocument().getFirmwareUrl();
		try {
			downloadFromUrl(firmwareUrl, "anil-poc.zip");

			deviceController.updateJob(jobExecution.getExecution().getJobId());
		} catch (IOException | AWSIotException e) {
			LOGGER.error("exception: {}", e);
		}
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