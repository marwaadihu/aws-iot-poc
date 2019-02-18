package com.anil.iot.portal.contoller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;

@RestController
public class S3DocumentController {

	@Autowired
	private AmazonS3 amazonS3Client;

	@GetMapping("listBuckets")
	public List<Bucket> listBuckets() {
		return amazonS3Client.listBuckets();
	}

	@GetMapping("listObjects")
	public ListObjectsV2Result listObjectsFromBuckets(@RequestParam("bucketName") String bucketName) {
		return amazonS3Client.listObjectsV2(bucketName);
	}

	@GetMapping("getDocumentUrl")
	public String getDocumentUrl(@RequestParam("bucketName") String bucketName, @RequestParam("key") String key) {
		return amazonS3Client.getUrl(bucketName, key).toExternalForm();
	}
	
	/**
	 * @param putObjectRequest
	 */
	@PutMapping("putDocument")
	public void putDocument(PutObjectRequest putObjectRequest) {
		amazonS3Client.putObject(putObjectRequest);
	}
}
