package com.anil.iot.portal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.amazonaws.services.iot.model.CreateJobRequest;
import com.amazonaws.services.iot.model.JobExecutionsRolloutConfig;
import com.amazonaws.services.iot.model.ListThingsResult;
import com.anil.iot.portal.contoller.S3DocumentController;
import com.anil.iot.portal.contoller.ThingController;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@WebMvcTest
public class JobControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ThingController thingController;

	@Autowired
	private S3DocumentController s3DocumentController;

	@Test
	public void listJobs() throws Exception {
		mockMvc.perform(get("/listJobs")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void createJob() throws Exception {
		CreateJobRequest createJobRequest = new CreateJobRequest();
		createJobRequest.setJobId("test-create-job");

		List<String> targets = new ArrayList<>();
		ListThingsResult listThings = thingController.listThings();
		targets.add(listThings.getThings().get(0).getThingArn());
		createJobRequest.setTargets(targets);

		String bucketName = "bigdatateambucket";
		String keyName = "rajan-job.json";
		String documentSource = s3DocumentController.getDocumentUrl(bucketName, keyName);
		createJobRequest.setDocumentSource(documentSource);

		JobExecutionsRolloutConfig jobExecutionsRolloutConfig = new JobExecutionsRolloutConfig();
		jobExecutionsRolloutConfig.setMaximumPerMinute(1);
		createJobRequest.setJobExecutionsRolloutConfig(jobExecutionsRolloutConfig);
		createJobRequest.setTargetSelection("SNAPSHOT");

		String inputContent = new Gson().toJson(createJobRequest).toString();

		mockMvc.perform(post("/createJob").content(inputContent).contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk());
	}

}
