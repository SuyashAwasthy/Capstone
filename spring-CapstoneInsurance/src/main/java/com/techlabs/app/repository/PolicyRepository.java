package com.techlabs.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

//	List<Policy> findByAgent(Agent agent);
}
