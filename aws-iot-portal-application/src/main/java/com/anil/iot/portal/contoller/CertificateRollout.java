package com.anil.iot.portal.contoller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.model.AttachPolicyRequest;
import com.amazonaws.services.iot.model.AttachThingPrincipalRequest;
import com.amazonaws.services.iot.model.CreateCertificateFromCsrRequest;
import com.amazonaws.services.iot.model.CreateCertificateFromCsrResult;
import com.amazonaws.services.iot.model.CreateJobRequest;
import com.amazonaws.services.iot.model.DescribeCertificateRequest;
import com.amazonaws.services.iot.model.DescribeCertificateResult;
import com.amazonaws.services.iot.model.JobExecutionsRolloutConfig;
import com.amazonaws.services.iot.model.ListAttachedPoliciesRequest;
import com.amazonaws.services.iot.model.ListAttachedPoliciesResult;
import com.amazonaws.services.iot.model.ListPrincipalThingsRequest;
import com.amazonaws.services.iot.model.ListPrincipalThingsResult;
import com.amazonaws.services.iot.model.ListThingsResult;
import com.amazonaws.services.iot.model.Policy;
import com.amazonaws.services.iot.model.ThingAttribute;
import com.amazonaws.services.s3.model.PutObjectRequest;

@RestController
public class CertificateRollout {

	private static final Logger LOGGER = LoggerFactory.getLogger(CertificateRollout.class);

	@Value("${aws.csr}")
	private String certificateSigningRequest;

	@Autowired
	private AWSIot awsIotClient;

	@Autowired
	private JobContoller jobContoller;

	@Autowired
	private ThingController thingController;

	@Autowired
	private S3DocumentController s3DocumentController;

	@GetMapping("/rolloutCertificate")
	public void rolloutCertificate(@RequestParam String certificateId) throws IOException {
		DescribeCertificateResult describeCertificate = describeCertificate(certificateId);

		List<Policy> policies = getAssignedPoliciesToCertificate(describeCertificate);

		List<String> things = getAssignedThingsToCertificate(describeCertificate);

		CreateCertificateFromCsrResult createKeysAndCertificate = createCertificate();

		attachPoliciesToCertificate(policies, createKeysAndCertificate);

		attachThingsToCertificate(things, createKeysAndCertificate);

		JSONObject jsonObject = getJSONForCertificate(createKeysAndCertificate, certificateId);

		LOGGER.info("{}", jsonObject);

		putDataToS3Bucket(jsonObject);

		createJob(things, certificateId);
	}

	/**
	 * @param things
	 * @param oldCertificateId
	 */
	private void createJob(List<String> things, String oldCertificateId) {
		CreateJobRequest createJobRequest = new CreateJobRequest();
		createJobRequest.setJobId("cert_update" + new Random().nextInt());

		List<String> targets = new ArrayList<>();

		ListThingsResult listThings = thingController.listThings(null, null, null);

		for (String thingName : things) {
			for (ThingAttribute thingsFromIOT : listThings.getThings()) {
				if (thingName.equals(thingsFromIOT.getThingName())) {
					targets.add(thingsFromIOT.getThingArn());
				}
			}
		}

		createJobRequest.setTargets(targets);

		createJobRequest.setDescription(oldCertificateId);

		String bucketName = "aniltestpoc";
		String keyName = "anil/certificates.json";
		String documentSource = s3DocumentController.getDocumentUrl(bucketName, keyName);
		createJobRequest.setDocumentSource(documentSource);

		JobExecutionsRolloutConfig jobExecutionsRolloutConfig = new JobExecutionsRolloutConfig();
		jobExecutionsRolloutConfig.setMaximumPerMinute(2);
		createJobRequest.setJobExecutionsRolloutConfig(jobExecutionsRolloutConfig);
		createJobRequest.setTargetSelection("SNAPSHOT");

		jobContoller.createJob(createJobRequest);
	}

	/**
	 * @param jsonObject
	 * @throws IOException
	 */
	private void putDataToS3Bucket(JSONObject jsonObject) throws IOException {
		LOGGER.info("saving data into s3 object");
		File file = File.createTempFile("tempFile", ".tmp");

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			bw.write(jsonObject.toString());
		}

		PutObjectRequest putObjectRequest = new PutObjectRequest("aniltestpoc", "anil/certificates.json", file);
		s3DocumentController.putDocument(putObjectRequest);
	}

	/**
	 * @param createKeysAndCertificate
	 * @param oldCertificateId
	 * @return
	 */
	private JSONObject getJSONForCertificate(CreateCertificateFromCsrResult createKeysAndCertificate,
			String oldCertificateId) {
		LOGGER.info("creating json for certifcate to create job");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("operation", "change_certificate");
		jsonObject.put("oldCertificateId", oldCertificateId);
		jsonObject.put("certificatePem", createKeysAndCertificate.getCertificatePem());
		return jsonObject;
	}

	/**
	 * @param things
	 * @param createKeysAndCertificate
	 */
	private void attachThingsToCertificate(List<String> things,
			CreateCertificateFromCsrResult createKeysAndCertificate) {
		LOGGER.info("attachThingsToCertificate");
		for (String thingName : things) {
			AttachThingPrincipalRequest attachThingPrincipalRequest = new AttachThingPrincipalRequest();
			attachThingPrincipalRequest.setPrincipal(createKeysAndCertificate.getCertificateArn());
			attachThingPrincipalRequest.setThingName(thingName);
			awsIotClient.attachThingPrincipal(attachThingPrincipalRequest);
			LOGGER.info("thing: {} assigned to the certificate: {}", thingName,
					createKeysAndCertificate.getCertificateArn());
		}
	}

	/**
	 * @param policies
	 * @param createKeysAndCertificate
	 */
	private void attachPoliciesToCertificate(List<Policy> policies,
			CreateCertificateFromCsrResult createKeysAndCertificate) {
		LOGGER.info("attachPoliciesToCertificate");
		for (Policy policy : policies) {
			AttachPolicyRequest attachPolicyRequest = new AttachPolicyRequest();
			attachPolicyRequest.setTarget(createKeysAndCertificate.getCertificateArn());
			attachPolicyRequest.setPolicyName(policy.getPolicyName());
			awsIotClient.attachPolicy(attachPolicyRequest);
			LOGGER.info("policy: {} assigned to the certificate: {}", policy.getPolicyName(),
					createKeysAndCertificate.getCertificateArn());
		}
	}

	/**
	 * @return
	 */
	private CreateCertificateFromCsrResult createCertificate() {
		LOGGER.info("createCertificate");

		CreateCertificateFromCsrRequest createCertificateFromCsrRequest = new CreateCertificateFromCsrRequest();
		createCertificateFromCsrRequest.setCertificateSigningRequest(certificateSigningRequest);
		createCertificateFromCsrRequest.setSetAsActive(true);
		CreateCertificateFromCsrResult createCertificateFromCsr = awsIotClient
				.createCertificateFromCsr(createCertificateFromCsrRequest);
		LOGGER.info("new certificate with arn: {} is created", createCertificateFromCsr.getCertificateArn());

		return createCertificateFromCsr;
	}

	/**
	 * @param describeCertificate
	 * @return
	 */
	private List<String> getAssignedThingsToCertificate(DescribeCertificateResult describeCertificate) {
		LOGGER.info("getAssignedThingsToCertificate");
		ListPrincipalThingsRequest listPrincipalThingsRequest = new ListPrincipalThingsRequest();
		listPrincipalThingsRequest.setPrincipal(describeCertificate.getCertificateDescription().getCertificateArn());
		ListPrincipalThingsResult listPrincipalThings = awsIotClient.listPrincipalThings(listPrincipalThingsRequest);
		return listPrincipalThings.getThings();
	}

	/**
	 * @param describeCertificate
	 * @return
	 */
	private List<Policy> getAssignedPoliciesToCertificate(DescribeCertificateResult describeCertificate) {
		LOGGER.info("getAssignedPoliciesToCertificate");
		ListAttachedPoliciesRequest listAttachedPoliciesRequest = new ListAttachedPoliciesRequest();
		listAttachedPoliciesRequest.setTarget(describeCertificate.getCertificateDescription().getCertificateArn());
		ListAttachedPoliciesResult listAttachedPolicies = awsIotClient
				.listAttachedPolicies(listAttachedPoliciesRequest);
		return listAttachedPolicies.getPolicies();
	}

	/**
	 * @param certificateId
	 * @return
	 */
	private DescribeCertificateResult describeCertificate(String certificateId) {
		LOGGER.info("describeCertificate");
		DescribeCertificateRequest describeCertificateRequest = new DescribeCertificateRequest();
		describeCertificateRequest.setCertificateId(certificateId);
		DescribeCertificateResult describeCertificate = awsIotClient.describeCertificate(describeCertificateRequest);
		LOGGER.info("expiration date for certificate: {}. is {}", certificateId,
				describeCertificate.getCertificateDescription().getValidity().getNotAfter());
		return describeCertificate;
	}

}
