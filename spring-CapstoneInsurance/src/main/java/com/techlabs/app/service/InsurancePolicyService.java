package com.techlabs.app.service;

import java.util.List;

import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.util.PagedResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface InsurancePolicyService  {

	String registerPolicyForCustomer(long customerId, long policyId, long agentId);

	InsuranceScheme getInsuranceScheme(Long insuranceSchemeId);

	String createPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId);

	PagedResponse<InsurancePolicyDto> getAllPoliciesUnderAnAgent(Long id, Long customerId, String name,
            String policyStatus, int page, int size, HttpServletRequest request);

	//List<InsurancePolicy> getPoliciesByCustomerId(Long customerId);

}
