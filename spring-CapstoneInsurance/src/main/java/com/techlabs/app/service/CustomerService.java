package com.techlabs.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.techlabs.app.dto.CancellationRequestDto;
import com.techlabs.app.dto.ChangePasswordDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.CustomerRequestDto;
import com.techlabs.app.dto.CustomerResponseDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.PolicyAccountRequestDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.util.PagedResponse;

public interface CustomerService {

//	void addCustomer(RegisterDto registerDto);
//
//	String createInsurancePolicy(InsurancePolicyDto insurancePolicyDto);
//
//	String buyPolicy(InsurancePolicyDto accountRequestDto, long customerId);
//
//	String buyPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId);
//
//	String claimPolicy(ClaimRequestDto claimRequestDto, long customerId);
//
//	String requestPolicyCancellation(CancellationRequestDto cancellationRequest, Long customerId);
//
//	String customerCancelPolicy(ClaimRequestDto claimRequestDto, Long customerId);
//
//	String buyPolicy(InsurancePolicyDto accountRequestDto, Customer customerId);
//
//	String buyPolicy(InsurancePolicyDto accountRequestDto, Long customerId);
//
//	void changePassword(Long customerId, ChangePasswordDto changePasswordDto);
//
//	CustomerResponseDto findCustomerByid(long customerId);
//
//	void editCustomerDetails(Long customerId, CustomerRequestDto customerRequestDto);
//
//	//InsurancePolicyDto getPolicyByid(Long customerId, Long policyId);
//
//	List<InsurancePolicyDto> getAllPoliciesByCustomerId(Long customerId);
//
//	List<Customer> getAllCustomers();
//
//	Page<Customer> getAllCustomers(Pageable pageable);
//
//	PagedResponse<CustomerResponseDto> getAllCustomers(int page, int size);

	void addCustomer(RegisterDto registerDto);

	List<Customer> getAllCustomers();

	String createInsurancePolicy(InsurancePolicyDto insurancePolicyDto);

//	String buyPolicy(InsurancePolicyDto accountRequestDto, long customerId);

	String buyPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId);

	String claimPolicy(ClaimRequestDto claimRequestDto, long customerId);

	String requestPolicyCancellation(CancellationRequestDto cancellationRequest, Long customerId);

	String customerCancelPolicy(ClaimRequestDto claimRequestDto, Long customerId);

	Page<Customer> getAllCustomers(Pageable pageable);
	
	InsurancePolicyDto buyPolicy(InsurancePolicyDto accountRequestDto, long customerId);


	
	//String buyPolicy(PolicyAccountRequestDto accountRequestDto, long customerId);

}
