package com.anil.iot.portal;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.AWSIotClientBuilder;
import com.amazonaws.services.iot.model.Configuration;
import com.amazonaws.services.iot.model.DescribeEventConfigurationsRequest;
import com.amazonaws.services.iot.model.DescribeEventConfigurationsResult;
import com.amazonaws.services.iot.model.UpdateEventConfigurationsRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@SpringBootApplication
public class AwsIotPortalApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(AwsIotPortalApplication.class);

	@Value("${aws.accessKeyId}")
	private String accessKeyId;

	@Value("${aws.secretAccessKey}")
	private String secretAccessKey;

	@Value("${aws.region}")
	private String region;

	private AWSCredentials getAwsCredentials() {
		return new BasicAWSCredentials(accessKeyId, secretAccessKey);
	}

	@Bean
	public AWSIot awsIotClient() {
		return AWSIotClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(getAwsCredentials()))
				.withRegion(region).build();
	}

	@Bean
	public AmazonS3 awsS3Client() {
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(getAwsCredentials()))
				.withRegion(region).build();
	}

	@PostConstruct
	public void init() {
		DescribeEventConfigurationsResult describeEventConfigurations = awsIotClient()
				.describeEventConfigurations(new DescribeEventConfigurationsRequest());
		if (!describeEventConfigurations.getEventConfigurations().get("JOB").getEnabled()) {
			UpdateEventConfigurationsRequest updateEventConfigurationsRequest = new UpdateEventConfigurationsRequest();
			Map<String, Configuration> eventConfigurations = new HashMap<>();
			Configuration configuration = new Configuration();
			configuration.setEnabled(true);
			eventConfigurations.put("JOB", configuration);
			updateEventConfigurationsRequest.setEventConfigurations(eventConfigurations);
			awsIotClient().updateEventConfigurations(updateEventConfigurationsRequest);
			LOGGER.info("job event configurations is changed to enabled");
		} else {
			LOGGER.info("job event configurations is already enabled");
		}

	}

	public static void main(String[] args) {
		SpringApplication.run(AwsIotPortalApplication.class, args);
	}

}
