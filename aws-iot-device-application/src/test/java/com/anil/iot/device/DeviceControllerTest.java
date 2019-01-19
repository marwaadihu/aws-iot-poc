package com.anil.iot.device;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest
public class DeviceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void startListening() throws Exception {
		mockMvc.perform(get("/startListening")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void startJob() throws Exception {
		mockMvc.perform(get("/startJob")).andDo(print()).andExpect(status().isOk());
	}

}
