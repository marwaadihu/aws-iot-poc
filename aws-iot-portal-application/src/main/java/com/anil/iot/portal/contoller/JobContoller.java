package com.anil.iot.portal.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.model.CreateJobRequest;
import com.amazonaws.services.iot.model.CreateJobResult;
import com.amazonaws.services.iot.model.DeleteJobRequest;
import com.amazonaws.services.iot.model.DeleteJobResult;
import com.amazonaws.services.iot.model.DescribeJobRequest;
import com.amazonaws.services.iot.model.DescribeJobResult;
import com.amazonaws.services.iot.model.ListJobsRequest;
import com.amazonaws.services.iot.model.ListJobsResult;
import com.amazonaws.services.iot.model.UpdateJobRequest;
import com.amazonaws.services.iot.model.UpdateJobResult;

@RestController
public class JobContoller {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobContoller.class);

	@Autowired
	private AWSIot awsIotClient;

	@PostMapping("createJob")
	public ResponseEntity<String> createJob(@RequestBody CreateJobRequest createJobRequest) {
		CreateJobResult createJob = awsIotClient.createJob(createJobRequest);
		LOGGER.info("{} created successfully", createJob.getJobId());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("updateJob")
	public ResponseEntity<String> updateJob(@RequestBody UpdateJobRequest updateJobRequest) {
		UpdateJobResult updateJob = awsIotClient.updateJob(updateJobRequest);
		LOGGER.info("{} updated with status code: {}", updateJobRequest.getJobId(),
				updateJob.getSdkHttpMetadata().getHttpStatusCode());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("deleteJob")
	public DeleteJobResult deleteJob(@RequestBody DeleteJobRequest deleteJobRequest) {
		return awsIotClient.deleteJob(deleteJobRequest);
	}

	@GetMapping("listJobs")
	public ListJobsResult listJobs() {
		return awsIotClient.listJobs(new ListJobsRequest());
	}

	@GetMapping("describeJob")
	public DescribeJobResult describeJob(@RequestParam("jobId") String jobId) {
		return awsIotClient.describeJob(new DescribeJobRequest().withJobId(jobId));
	}

}
