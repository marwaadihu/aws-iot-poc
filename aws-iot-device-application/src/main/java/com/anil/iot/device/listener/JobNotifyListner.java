package com.anil.iot.device.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotTopic;

public class JobNotifyListner extends AWSIotTopic {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobNotifyListner.class);

	public JobNotifyListner(String topic) {
		super(topic);
	}

	@Override
	public void onMessage(AWSIotMessage message) {
		LOGGER.info("message received: {}", message.getStringPayload());
	}

}
