package com.java.reward.program;

import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.java.reward.program.controller.RewardProgramController;
import com.java.reward.program.entity.Transaction;
import com.java.reward.program.modal.RewardPointResponse;
import com.java.reward.program.service.RewardServiceImplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class RewardProgramControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private RewardServiceImplementation rewardServiceImplementation;

	@InjectMocks
	private RewardProgramController rewardProgramController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(rewardProgramController).build();
	}

	
	@Test
	public void testCreateTransaction() throws Exception {
		Transaction transaction = new Transaction();
		transaction.setCustomerId(1L);

		LocalDate localDate = LocalDate.parse("2024-08-12");
		transaction.setTransactionDate(localDate);
		transaction.setAmount(200.0);

		MvcResult result = mockMvc.perform(post("/api/v1/createTransactions")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(transaction)))
				.andExpect(status().isOk())
				.andReturn();

		String response = result.getResponse().getContentAsString();
		assertEquals("Transaction created successfully", response);
	}

	
	private String asJsonString(Object obj) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testGetRewardPointsBasedOnCustomerId() throws Exception {
		Long customerId = 1L;
		RewardPointResponse mockResponse = new RewardPointResponse(Map.of("jan", 100), 100);

		when(rewardServiceImplementation.getRewardPointsByCustomer(customerId))
				.thenReturn(mockResponse);

		String expectedResponse = new ObjectMapper().writeValueAsString(mockResponse);

		mockMvc.perform(get("/api/v1/customer/1")).andExpect(status().isOk()).andExpect(result -> {
			String actualResponse = result.getResponse().getContentAsString();
			assertEquals(expectedResponse, actualResponse);
		});
	}

	@Test
	public void testGetRewardPointsBasedOnCustomerId_InvalidCustomerIdFormat() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/v1/customer/abc"))
				.andExpect(status().isBadRequest()).andExpect(content().string("Invalid customer ID format")).andReturn();
		String response = result.getResponse().getContentAsString();
		assertEquals("Invalid customer ID format", response);
	}
	
	@Test
	public void testGetRewardPointsBasedOnCustomerId_NegativeCustomerId() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/v1/customer/-1")) 
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Customer ID must be a positive number")).andReturn();
		String response = result.getResponse().getContentAsString();
		assertEquals("Customer ID must be a positive number", response);
	}
}