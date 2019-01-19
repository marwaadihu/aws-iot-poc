package com.anil.iot.device.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.iot.client.AWSIotConnectionStatus;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;

@Component
public class DeviceUtil {

	@Value("${aws.thingName}")
	private String thingName;

	@Autowired
	private AWSIotMqttClient awsIotMqttClient;

	public String getQualifiedTopicName(String topic) {
		return topic.replace("thingName", thingName);
	}

	public String getQualifiedTopicName(String topic, String jobId) {
		return topic.replace("thingName", thingName).replace("jobId", jobId);
	}

	public void connectAwsIotMqttClient() throws AWSIotException {
		if (AWSIotConnectionStatus.DISCONNECTED.equals(awsIotMqttClient.getConnectionStatus())) {
			awsIotMqttClient.connect();
		}
	}

}
