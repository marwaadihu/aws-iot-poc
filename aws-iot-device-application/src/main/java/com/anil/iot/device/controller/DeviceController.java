package com.anil.iot.device.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.iot.client.AWSIotException;
import com.anil.iot.device.configuration.Configuration;
import com.anil.iot.device.listener.JobNotifyListner;
import com.anil.iot.device.listener.StartNextJobListener;
import com.anil.iot.device.util.Constants;
import com.anil.iot.device.util.DeviceUtil;

@RestController
public class DeviceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

	@Autowired
	private DeviceUtil deviceUtil;

	@Autowired
	private Configuration configuration;

	@GetMapping("startListening")
	public void startListening() throws AWSIotException {

		deviceUtil.connectAwsIotMqttClient();

		JobNotifyListner jobNotifyListner = new JobNotifyListner(
				deviceUtil.getQualifiedTopicName(Constants.TOPIC_JOBS_NOTIFY));
		configuration.getAwsIot().subscribe(jobNotifyListner, true);

		LOGGER.info("startListening --> subscribed successfully");
	}

	@GetMapping("startJob")
	public void startJob() throws AWSIotException {
		deviceUtil.connectAwsIotMqttClient();
		String payload = "{ \n" + "    \"statusDetails\": {\n" + "        \"string\": \"IN_PROGRESS\"\n" + "    },\n"
				+ "    \"stepTimeoutInMinutes\": \"2\",\n" + "    \"clientToken\": \"anil-poc\"\n" + "}";
		configuration.getAwsIot().publish(deviceUtil.getQualifiedTopicName(Constants.TOPIC_JOBS_START_NEXT), payload);
		LOGGER.info("startJob --> message published successfully");

		StartNextJobListener startNextJobListener = new StartNextJobListener(
				deviceUtil.getQualifiedTopicName(Constants.TOPIC_JOBS_START_NEXT_ACCEPTED), this);
		configuration.getAwsIot().subscribe(startNextJobListener);

		LOGGER.info("startJob/accepted --> subscribed successfully");
	}

	public void updateJob(String jobId) throws AWSIotException {
		deviceUtil.connectAwsIotMqttClient();
		String payload = "{ \"status\": \"SUCCEEDED\"}";
		configuration.getAwsIot().publish(deviceUtil.getQualifiedTopicName(Constants.TOPIC_JOBS_UPDATE, jobId),
				payload);

		LOGGER.info("updateJob --> message published successfully");
	}

}
