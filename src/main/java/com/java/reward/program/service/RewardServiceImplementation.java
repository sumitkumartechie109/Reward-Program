package com.java.reward.program.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.reward.program.entity.Transaction;
import com.java.reward.program.modal.RewardPointResponse;
import com.java.reward.program.repository.TransactionRepository;



@Service
public class RewardServiceImplementation implements RewardService {

	@Autowired
	private TransactionRepository transactionsRepository;
	
	
	/**
	 * This method calculate the reward points
	 * 
	 * @param transactionAmount
	 */
	
	@Override
	public int calculateRewardPoints(Double transactionAmount) {
		int points = 0;

		// Calculate points for amount spent over $100
		if (transactionAmount > 100) {
			double amountOver100 = transactionAmount - 100;
			points += amountOver100 * 2; // 2 points for every dollar over $100
		}

		// Calculate points for amount spent between $50 and $100
		if (transactionAmount >= 50 && transactionAmount <=100 ) {
			double amountBetween50And100 = Math.min(transactionAmount, 100) - 50;
			points += amountBetween50And100; // 1 point for every dollar between $50 and $100
		}

		return points;
	}

	/**
	 * This method create a new customer records
	 * 
	 * @param customerId
	 * @param amount
	 * @param transactionDate
	 */
	public void calculateAndStoreRewardPoints(Long customerId, Double amount, LocalDate transactionDate) {
		validateTransaction(customerId, amount, transactionDate);
		Transaction customerRewardPoints = new Transaction();
		customerRewardPoints.setCustomerId(customerId);
		customerRewardPoints.setAmount(amount);
		customerRewardPoints.setTransactionDate(transactionDate);
		customerRewardPoints.setRewardPoint(calculateRewardPoints(amount));
		transactionsRepository.save(customerRewardPoints);
	}
	
	
	/**
	 * This method calculate the get reward points by customer id
	 * 
	 * @param customerId
	 */
	public RewardPointResponse getRewardPointsByCustomer(Long customerId) {
		List<Transaction> transactionsList = transactionsRepository.findByCustomerId(customerId);
		Map<String, Integer> monthlyPoints = new HashMap<>();
		int totalPoints = 0;
		for (Transaction transaction : transactionsList) {
			String month = transaction.getTransactionDate().getMonth().getDisplayName(TextStyle.SHORT,
					Locale.getDefault());
			int points = calculateRewardPoints(transaction.getAmount());
			monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
			totalPoints += points;
		}
		
		RewardPointResponse rewardPointResponse = new RewardPointResponse(monthlyPoints, totalPoints);
		return rewardPointResponse;
	}

	/**
	 * this method is for validating the user inputs
	 * 
	 * @param customerId
	 * @param amount
	 * @param transactionDate
	 */
	private void validateTransaction(Long customerId, Double amount, LocalDate transactionDate) {
		if (customerId == null) {
			throw new IllegalArgumentException("Customer ID cannot be null");
		}
		if (customerId < 0) {
			throw new IllegalArgumentException("Customer ID cannot be a negative value");
		}
		if (transactionDate == null) {
			throw new IllegalArgumentException("Transaction date cannot be null");
		}
		if (amount < 0) {
			throw new IllegalArgumentException("Amount cannot be less than zero");
		}

		try {
			String formattedDate = transactionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			if (!formattedDate
					.equals(transactionDate.getYear() + "-" + String.format("%02d", transactionDate.getMonthValue())
							+ "-" + String.format("%02d", transactionDate.getDayOfMonth()))) {
				throw new IllegalArgumentException("Transaction date must be in the format yyyy-MM-dd");
			}
		} catch (DateTimeException e) {
			throw new IllegalArgumentException("Invalid transaction date", e);
		}
	}
}