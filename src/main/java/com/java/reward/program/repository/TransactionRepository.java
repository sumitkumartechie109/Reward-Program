package com.java.reward.program.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.reward.program.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	
	List<Transaction> findByCustomerId(Long customerId);

}