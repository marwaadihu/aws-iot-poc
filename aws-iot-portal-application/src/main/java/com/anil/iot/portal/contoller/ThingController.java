package com.anil.iot.portal.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.model.ListThingsRequest;
import com.amazonaws.services.iot.model.ListThingsResult;

@RestController
public class ThingController {

	@Autowired
	private AWSIot awsIotClient;

	@GetMapping("listThings")
	public ListThingsResult listThings() {
		return awsIotClient.listThings(new ListThingsRequest());
	}

}
