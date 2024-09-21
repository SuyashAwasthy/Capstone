package com.techlabs.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.NomineeDto;
import com.techlabs.app.dto.UserResponseDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Nominee;
import com.techlabs.app.entity.Payment;
import com.techlabs.app.entity.SubmittedDocument;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.AllExceptions;
import com.techlabs.app.exception.NoRecordFoundException;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.repository.UserRepository;
import com.techlabs.app.security.JwtTokenProvider;
import com.techlabs.app.util.PagedResponse;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class InsurancePolicyServiceImpl implements InsurancePolicyService {

	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public String registerPolicyForCustomer(long customerId, long policyId, long agentId) {
//		    Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
//		        InsurancePolicy policy = insurancePolicyRepository.findById(policyId).orElseThrow(() -> new RuntimeException("Policy not found"));     
//		        Agent agent = agentService.findAgentById(agentId);
//		        if (!customer.getCity().equals(agent.getCity())) {
//		        	
//		            throw new RuntimeException("Agent and customer must be in the same city.");        }
//		        policy.setAgent(agent); // Assign agent to the policy
//		        policy.getCustomers().add(customer); // Link customer to the policy        insurancePolicyRepository.save(policy);
		return "Policy registered successfully.";
//		    }
//		}
	}

	@Override
	public InsuranceScheme getInsuranceScheme(Long insuranceSchemeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId) {
		// TODO Auto-generated method stub
		return null;
	}
//	@Override
//	public List<InsurancePolicy> getPoliciesByCustomerId(Long customerId) {
//		// TODO Auto-generated method stub
//		return insurancePolicyRepository.findByCustomersId(customerId);
//	}

	@Override
	public PagedResponse<InsurancePolicyDto> getAllPoliciesUnderAnAgent(Long id, Long customerId, String name,
			String policyStatus, int page, int size, HttpServletRequest request) {

		final String authHeader = request.getHeader("Authorization");
		final String token = authHeader.substring(7);
		String username = jwtTokenProvider.getUsername(token);
		User user = userRepository.findByUsernameOrEmail(username, username).orElseThrow(
				() -> new NoRecordFoundException("User with username or email : " + username + " cannot be found"));

	//	UserResponseDto user = authService.getLoggedUser(request);
		Long agentId = user.getId();
		Pageable pageable = PageRequest.of(page, size);
		Page<InsurancePolicy> policies = insurancePolicyRepository.findAllPoliciesBasedOnSearch(id, customerId, agentId,
				null, null, name, policyStatus, pageable);

		if (policies.isEmpty()) {
			throw new NoRecordFoundException("No Policies Found!");
		}

		List<InsurancePolicyDto> response = new ArrayList<>();
//		for(InsurancePolicy policy : policies) {
//			
//			InsurancePolicyDto policyDto = new InsurancePolicyDto();
//			policyDto.setActive(policy.isActive());
//			policyDto.setAgentId(policy.getAgent());
//			policyDto.setClaimId(policy.getClaim());
//			policyDto.setCustomerIds(policy.getCustomers());
//			policyDto.setDocumentIds(policy.getDocuments());
//			policyDto.setDocuments(policy.getDocuments());
//			policyDto.setInstallmentPeriod(policy.getInstallmentPeriod());
//			policyDto.setInsuranceId(policy.getInsuranceId());
//			policyDto.setInsuranceScheme(policy.getInsuranceScheme());
//			policyDto.setInsuranceSchemeId();
//			policyDto.setIssuedDate(policy.getIssuedDate());
//			policyDto.setMaturityDate(policy.getMaturityDate());
//			policyDto.setNomineeIds();
//			policyDto.setNominees(policy.getNominees());
//			policyDto.setPaymentIds();
//			policyDto.setPolicyStatus(policy.getPolicyStatus());
//			policyDto.setPolicyTerm(policy.getPolicyTerm());
//			policyDto.setPremiumAmount(policy.getPremiumAmount());
//			
//		}
		
		for(InsurancePolicy policy : policies) {
		    InsurancePolicyDto policyDto = new InsurancePolicyDto();
		    
		    // Direct mappings
		    policyDto.setActive(policy.isActive());
		    policyDto.setAgentId(policy.getAgent().getAgentId()); // Assuming Agent has getAgentId()
		    policyDto.setClaimId(policy.getClaim() != null ? policy.getClaim().getClaimId() : null); // Handle null cases
		    policyDto.setInsuranceId(policy.getInsuranceId());
		    policyDto.setInsuranceSchemeId(policy.getInsuranceScheme().getInsuranceSchemeId()); // Assuming InsuranceScheme has getSchemeId()
		    policyDto.setIssuedDate(policy.getIssuedDate());
		    policyDto.setMaturityDate(policy.getMaturityDate());
		    policyDto.setPolicyStatus(policy.getPolicyStatus());
		    policyDto.setPolicyTerm(policy.getPolicyTerm());
		    policyDto.setPremiumAmount(policy.getPremiumAmount());
		    policyDto.setInstallmentPeriod(policy.getInstallmentPeriod());

		    // List and Set mappings (from entities to IDs)
		    List<Long> nomineeIds = policy.getNominees().stream().map(Nominee::getId).collect(Collectors.toList()); // Assuming Nominee has getNomineeId()
		    policyDto.setNomineeIds(nomineeIds);
		    
		    List<Long> paymentIds = policy.getPayments().stream().map(Payment::getId).collect(Collectors.toList()); // Assuming Payment has getPaymentId()
		    policyDto.setPaymentIds(paymentIds);
		    
		    Set<Long> documentIds = policy.getDocuments().stream().map(SubmittedDocument::getId).collect(Collectors.toSet()); // Assuming SubmittedDocument has getDocumentId()
		    policyDto.setDocumentIds(documentIds);
		    
		    List<Long> customerIds = policy.getCustomers().stream().map(Customer::getCustomerId).collect(Collectors.toList()); // Assuming Customer has getCustomerId()
		    policyDto.setCustomerIds(customerIds);
		    
		    // Mapping full objects (if required)
//		    policyDto.setNominees(policy.getNominees().stream().map(nominee -> new NomineeDto(nominee.getId(), nominee.getNomineeName(), nominee.getRelationStatus())).collect(Collectors.toList())); // Assuming you want to map Nominees to NomineeDto
//		    policyDto.setDocuments(policy.getDocuments().stream().map(doc -> new SubmittedDocumentDto(doc.getDocumentId(), doc.getDocumentType(), doc.getDocumentUrl())).collect(Collectors.toList())); // Assuming SubmittedDocumentDto takes these fields

		    // Add the policyDto to your list or perform further operations as needed
		}

		
//				policies.getContent().stream().map(insurancePolicyMapper::entityToDto)
//				.collect(Collectors.toList());
		

		return new PagedResponse<>(response, policies.getNumber(), policies.getNumberOfElements(),
				policies.getTotalElements(), policies.getTotalPages(), policies.isLast());
	}

}
