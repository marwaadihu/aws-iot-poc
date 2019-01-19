package com.anil.iot.portal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.AWSIotClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@SpringBootApplication
public class AwsIotPortalApplication {

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

	public static void main(String[] args) {
		SpringApplication.run(AwsIotPortalApplication.class, args);
	}

}
