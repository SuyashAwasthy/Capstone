package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
//    List<Transaction> findAllByAgentAndType(Agent agent, String type);

    List<Transaction> findAllByAgentAndTransactionType(Agent agent, String transactionType);

	
}
