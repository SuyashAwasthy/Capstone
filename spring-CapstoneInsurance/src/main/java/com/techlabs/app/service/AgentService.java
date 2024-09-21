package com.techlabs.app.service;

import java.util.List;
import java.util.Map;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.ChangePasswordDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.ClaimResponseDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.util.PagedResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface AgentService {

	String registerAgent(AgentRequestDto agentRequestDto);

	AgentResponseDto getAgentById(Long id);

	AgentResponseDto updateAgentProfile(Long id, AgentRequestDto agentRequestDto);

//	void changePassword(Long id, String newPassword);

	double calculateCommission(Long agentId, Long policyId);

//	void withdrawCommission(Long agentId, double amount);

	List<Double> getEarningsReport(Long agentId);

	List<Double> getCommissionReport(Long agentId);

	String agentclaimPolicy(ClaimRequestDto claimRequestDto, Long agentId);

	void changePassword(Long id, ChangePasswordDto changePasswordDto);

//	void withdrawCommission(Long agentId, double amount, Long insurancePolicyId);

    void withdrawCommission(Long agentId, double amount, Long insurancePolicyId);

	InsurancePolicy registerPolicy(Long agentId, Map<String, Object> policyDetails);

	List<Customer> getAllCustomers();
	
//	PagedResponse<InsurancePolicyDto> getAllPoliciesUnderAnAgent(Long id, Long customerId, String name,
//            String policyStatus, int page, int size, HttpServletRequest request);


}
