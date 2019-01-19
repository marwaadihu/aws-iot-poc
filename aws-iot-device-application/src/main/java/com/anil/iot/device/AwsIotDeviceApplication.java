package com.anil.iot.device;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.anil.iot.device.util.AwsUtil;
import com.anil.iot.device.util.AwsUtil.KeyStorePasswordPair;

@SpringBootApplication
public class AwsIotDeviceApplication {

	@Value("${aws.clientEndpoint}")
	private String clientEndpoint;

	@Value("${aws.clientId}")
	private String clientId;

	@Value("${aws.certificateFile}")
	private String certificateFile;

	@Value("${aws.privateKeyFile}")
	private String privateKeyFile;

	@Bean
	public AWSIotMqttClient awsIotMqttClient() {
		KeyStorePasswordPair pair = AwsUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile, null);
		return new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
	}

	public static void main(String[] args) {
		SpringApplication.run(AwsIotDeviceApplication.class, args);
	}

}
