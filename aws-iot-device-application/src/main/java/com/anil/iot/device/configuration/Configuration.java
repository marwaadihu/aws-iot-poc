package com.anil.iot.device.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.anil.iot.device.util.AwsUtil;
import com.anil.iot.device.util.AwsUtil.KeyStorePasswordPair;

/**
 * @author anil
 *
 */
@org.springframework.context.annotation.Configuration
public class Configuration {

	@Value("${aws.clientEndpoint}")
	private String clientEndpoint;

	@Value("${aws.clientId}")
	private String clientId;

	@Value("${aws.certificateFile}")
	private String certificateFile;

	@Value("${aws.privateKeyFile}")
	private String privateKeyFile;

	private AWSIotMqttClient awsIot;

	/**
	 * @param clientEndpoint the clientEndpoint to set
	 */
	public void setClientEndpoint(String clientEndpoint) {
		this.clientEndpoint = clientEndpoint;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * @param certificateFile the certificateFile to set
	 */
	public void setCertificateFile(String certificateFile) {
		this.certificateFile = certificateFile;
	}

	/**
	 * @param privateKeyFile the privateKeyFile to set
	 */
	public void setPrivateKeyFile(String privateKeyFile) {
		this.privateKeyFile = privateKeyFile;
	}

	/**
	 * @return the awsIot
	 */
	public AWSIotMqttClient getAwsIot() {
		return awsIot;
	}

	/**
	 * Method used to set AwsIot
	 */
	@Bean
	public void setAwsIot() {
		KeyStorePasswordPair pair = AwsUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile, null);
		this.awsIot = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
	}

}
