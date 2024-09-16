package com.java.reward.program.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.reward.program.entity.Transaction;
import com.java.reward.program.modal.RewardPointResponse;
import com.java.reward.program.service.RewardServiceImplementation;


@RestController
@RequestMapping("/api/v1")
public class RewardProgramController {

	
	@Autowired
	private RewardServiceImplementation rewardServiceImplementation;
	
	
	/**
	 * The createTransaction helps to create new transactions for
	 * customer
	 * 
	 * @param transaction
	 * @return
	 */
	@PostMapping("/createTransactions")
	public ResponseEntity<String> createTransaction(@RequestBody Transaction transaction) {
		try {
			rewardServiceImplementation.calculateAndStoreRewardPoints(transaction.getCustomerId(),
					transaction.getAmount(), transaction.getTransactionDate());
			return ResponseEntity.ok("Transaction created successfully");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request");
		}

	}
	
	
	/**
	 * The getRewardPointsBasedOnCustomerId returns RewardPointResponse 
	 * from the database
	 * 
	 * @param customerId
	 * @return
	 */
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<Object> getRewardPointsBasedOnCustomerId(@PathVariable String customerId) {
		try {
			Long custId = Long.parseLong(customerId);
			if (custId <= 0) {
				throw new IllegalArgumentException("Customer ID must be a positive number");
			}
			RewardPointResponse response = rewardServiceImplementation.getRewardPointsByCustomer(custId);
			return ResponseEntity.ok(response);
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().body("Invalid customer ID format");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request");
		}
	}

}
