package com.techlabs.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.CancellationRequestDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.NomineeDto;
import com.techlabs.app.dto.PolicyAccountRequestDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.dto.SubmittedDocumentDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.ClaimStatus;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Nominee;
import com.techlabs.app.entity.PolicyStatus;
import com.techlabs.app.entity.PremiumType;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.SubmittedDocument;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.AllExceptions;
import com.techlabs.app.exception.BankApiException;
import com.techlabs.app.exception.ResourceNotFoundException;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.ClaimRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.DocumentRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.repository.InsuranceSchemeRepository;
import com.techlabs.app.repository.KeyValueRepository;
import com.techlabs.app.repository.NomineeRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AgentRepository agentRepository;

	@Autowired
	private InsuranceSchemeRepository insuranceSchemeRepository;

	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;
	
	@Autowired
	private NomineeRepository nomineeRepository;
	
	@Autowired
	private ClaimRepository claimRepository;
	
	@Autowired
private DocumentRepository documentRepository;
	
	@Autowired
	private KeyValueRepository keyValueRepository;
	

	public CustomerServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			CustomerRepository customerRepository, CityRepository cityRepository, PasswordEncoder passwordEncoder,
			AgentRepository agentRepository, InsuranceSchemeRepository insuranceSchemeRepository,
			InsurancePolicyRepository insurancePolicyRepository, NomineeRepository nomineeRepository,
			ClaimRepository claimRepository) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.customerRepository = customerRepository;
		this.cityRepository = cityRepository;
		this.passwordEncoder = passwordEncoder;
		this.agentRepository = agentRepository;
		this.insuranceSchemeRepository = insuranceSchemeRepository;
		this.insurancePolicyRepository = insurancePolicyRepository;
		this.nomineeRepository = nomineeRepository;
		this.claimRepository = claimRepository;
	}

	@Override
	@Transactional

	public void addCustomer(RegisterDto registerDto) {
		// Check if the username or email already exists
		if (userRepository.existsByUsername(registerDto.getUsername())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
		}

		if (userRepository.existsByEmail(registerDto.getEmail())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
		}

		// Create a new user
		User user = new User();
		user.setUsername(registerDto.getUsername());
		user.setEmail(registerDto.getEmail());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		// Assign customer role to the user
		Set<Role> roles = new HashSet<>();
		Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
				.orElseThrow(() -> new BankApiException(HttpStatus.BAD_REQUEST, "Customer role not found"));
		roles.add(customerRole);
		user.setRoles(roles);

		// Save user to the repository
		userRepository.save(user);

		// Find the city using cityId
		City city = cityRepository.findById(registerDto.getCityId())
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "City not found"));

		// Create a new customer
		Customer customer = new Customer();
		customer.setUser(user);
		customer.setFirstName(registerDto.getFirstName());
		customer.setLastName(registerDto.getLastName());
		customer.setPhoneNumber(registerDto.getPhone_number());
		customer.setDob(registerDto.getDob());
		customer.setCity(city);
		customer.setActive(true); // Set default status as active
		customer.setVerified(false); // Set default verification status

		// Save customer to the repository
		customerRepository.save(customer);
	}

	@Override
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

//	@Override
//	public String createInsurancePolicy(InsurancePolicyDto insurancePolicyDto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	

	@Override
	public String createInsurancePolicy(InsurancePolicyDto insurancePolicyDto) {
		// Create a new InsurancePolicy entity
		InsurancePolicy policy = new InsurancePolicy();

		// Map basic fields from the DTO to the entity
		policy.setIssuedDate(insurancePolicyDto.getIssuedDate());
		policy.setMaturityDate(insurancePolicyDto.getMaturityDate());
		policy.setPremiumAmount(insurancePolicyDto.getPremiumAmount());
		policy.setPolicyStatus(insurancePolicyDto.getPolicyStatus());
	//	policy.setActive(insurancePolicyDto.isActive());

		// Fetch and set associated InsuranceScheme entity
		InsuranceScheme scheme = insuranceSchemeRepository.findById(insurancePolicyDto.getInsuranceSchemeId())
				.orElseThrow(() -> new RuntimeException(
						"Insurance Scheme not found for ID: " + insurancePolicyDto.getInsuranceSchemeId()));
		policy.setInsuranceScheme(scheme);

		// Fetch and set associated Agent entity
		Agent agent = agentRepository.findById(insurancePolicyDto.getAgentId())
				.orElseThrow(() -> new RuntimeException("Agent not found for ID: " + insurancePolicyDto.getAgentId()));
		policy.setAgent(agent);

		insurancePolicyRepository.save(policy);

		return "Insurance Policy created successfully.";
	}
//
//	@Override
//	public String buyPolicy(PolicyAccountRequestDto accountRequestDto, long customerId) {
//		System.out.println("Received PolicyAccountRequestDto: " + accountRequestDto);
//		 if (accountRequestDto.getInsuranceSchemeId() == null) {
//		        throw new IllegalArgumentException("Insurance scheme ID must not be null");
//		    }
//		    if (accountRequestDto.getAgentId() == null) {
//		        throw new IllegalArgumentException("Agent ID must not be null");
//		    }
//		
////		InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(null).orElseThrow(() -> new ResourceNotFoundException(
////				"Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));
//		    InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
//		            .orElseThrow(() -> new ResourceNotFoundException(
//		                "Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));
//		    System.out.println("gagdgdhdhhhhhhhhhhhhuwysh");
//		   System.out.println(insuranceScheme);
//		Customer customer = customerRepository.findById(customerId)
//				.orElseThrow(() -> new ResourceNotFoundException("Sorry, we couldn't find a customer with ID: " + customerId));
//		Agent agent = agentRepository.findById(accountRequestDto.getAgentId()).orElseThrow(
//				() -> new ResourceNotFoundException("Sorry, we couldn't find a agent with ID: " + accountRequestDto.getAgentId()));
//		InsurancePolicy policyAccount = new InsurancePolicy();
//		policyAccount.setCustomer(customer);
//		policyAccount.setAgent(agent);
//
////	      
////	      private Double installmentAmount;
////	      
////	      private Double totalPaidAmount;
//		policyAccount.setPremiumType(accountRequestDto.getPremiumType());
//		policyAccount.setPolicyTerm(accountRequestDto.getPolicyTerm());
//		policyAccount.setPremiumAmount(accountRequestDto.getPremiumAmount());
//		policyAccount.setMaturityDate(policyAccount.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
//		policyAccount.setSumAssured((policyAccount.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
//				+ policyAccount.getPremiumAmount());
//		long months;
//		if (policyAccount.getPremiumType().equals(PremiumType.MONTHLY)) {
//			months = 1;
//		} else if (policyAccount.getPremiumType().equals(PremiumType.QUARTERLY)) {
//			months = 3;
//		} else if (policyAccount.getPremiumType().equals(PremiumType.HALF_YEARLY)) {
//			months = 6;
//		} else {
//			months = 12;
//		}
//		double amount = policyAccount.getPremiumAmount();
//		long totalMonths = (policyAccount.getPolicyTerm() * 12) / months;
//		policyAccount.setInstallmentBalance(amount / totalMonths);
//		policyAccount.setClaimAmount(0.0);
//		return "Query has been successfully created for customer ID " + customerId + ".";
//	}
// ---------------------------------
	@Override
	public String buyPolicy(InsurancePolicyDto accountRequestDto, long customerId) {
		
		if (accountRequestDto.getInsuranceSchemeId() == null) {
	        throw new APIException(HttpStatus.BAD_REQUEST, "Insurance scheme ID must not be null");
	    }

	    InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
	            "Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));

	    Customer customer = customerRepository.findById(customerId)
	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
	            "Sorry, we couldn't find a customer with ID: " + customerId));

	    Agent agent = agentRepository.findById(accountRequestDto.getAgentId())
	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
	            "Sorry, we couldn't find an agent with ID: " + accountRequestDto.getAgentId()));

	    // Create a new InsurancePolicy and set its properties
	    InsurancePolicy insurancePolicy = new InsurancePolicy();

	    insurancePolicy.getCustomers().add(customer);
	    
	    insurancePolicy.setAgent(agent);

	    insurancePolicy.setPolicyTerm(accountRequestDto.getPolicyTerm());
	    insurancePolicy.setPremiumAmount(accountRequestDto.getPremiumAmount());
	    insurancePolicy.setIssuedDate(LocalDate.now()); // Assuming issued date is set to the current date
	    insurancePolicy.setMaturityDate(insurancePolicy.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
	    insurancePolicy.setInstallmentPeriod(accountRequestDto.getInstallmentPeriod());
	    insurancePolicy.setRegisteredCommission(insuranceScheme.getNewRegistrationCommission());
	    insurancePolicy.setInsuranceScheme(insuranceScheme);
	    insurancePolicy.setActive(true);
	    
	    
	    //handle nominees
	    if (accountRequestDto.getNominees() != null && !accountRequestDto.getNominees().isEmpty()) {
	    	System.out.println("checking for nominees---------------------------------------------------");
	        List<Nominee> nominees = new ArrayList<>();
	    //    for (NomineeDto nomineeDto : accountRequestDto.getNominees()) {
	        for(NomineeDto nomineeDto:accountRequestDto.getNominees()) {
	            Nominee nominee = new Nominee();
	            nominee.setNomineeName(nomineeDto.getNomineeName());
	            nominee.setRelationStatus(nomineeDto.getRelationStatus());
	            
	           nominee =nomineeRepository.save(nominee);
	            nominees.add(nominee);
	            
	        	//InsuranceScheme nominee=nomineeRepository.findById(nomineeId).orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"nominee not found withID: "+nomineeId));
	        }
	        insurancePolicy.setNominees(nominees);
	        System.out.println(nominees);// Add nominees to the policy
	    }
	    
	    double totalCommission = agent.getTotalCommission() + insurancePolicy.getRegisteredCommission();
	    agent.setTotalCommission(totalCommission);
	    
	    // Calculate the sum assured based on the profit ratio
	    double sumAssured = (insurancePolicy.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
	        + insurancePolicy.getPremiumAmount();
	    insurancePolicy.setClaimAmount(sumAssured);
	    insurancePolicy.setPolicyStatus(PolicyStatus.ACTIVE.name());

	    // Determine the number of months based on the premium type
	    long months= accountRequestDto.getInstallmentPeriod();
	// Calculate the total number of months and the installment amount
	    long totalMonths = insurancePolicy.getPolicyTerm() * 12 / months;
	    double installmentAmount = insurancePolicy.getPremiumAmount() / totalMonths;
	    insurancePolicy.setInstallmentPayment(installmentAmount);
	    insurancePolicy.setTotalAmountPaid(0.0);
	    
	 // Handle Documents
	      if (accountRequestDto.getDocuments() != null && !accountRequestDto.getDocuments().isEmpty()) {
	          Set<SubmittedDocument> documents = new HashSet<>();
	          for (SubmittedDocumentDto documentDto : accountRequestDto.getDocuments()) {
	              SubmittedDocument document = new SubmittedDocument();
	              document.setDocumentName(documentDto.getDocumentName());
	              document.setDocumentStatus(documentDto.getDocumentStatus()); // Setting document status
	              document.setDocumentImage(documentDto.getDocumentImage()); // Setting document image
	              documentRepository.save(document); // Save the new document to the repository
	              documents.add(document);
	          }
	          insurancePolicy.setDocuments(documents);
	      } else {
	          throw new APIException(HttpStatus.NOT_FOUND, "No documents provided in the request");
	      }

	      // Save the insurance policy to the repository
	      insurancePolicyRepository.save(insurancePolicy);

	      // Update the customer's list of policies
	      customer.getInsurancePolicies().add(insurancePolicy);
	      customerRepository.save(customer);

	    // Save the insurance policy to the repository
	    insurancePolicyRepository.save(insurancePolicy);

	    return "Policy has been successfully created for customer ID " + customerId + ".";
	  }
// --------------------------
//	@Override
//	  @Transactional
//	  public String buyPolicy(InsurancePolicyDto accountRequestDto, long customerId) {
//	      // Fetch the required entities (InsuranceScheme, Customer, Agent)
//	      InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
//	              .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
//	                      "Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));
//
//	      Customer customer = customerRepository.findById(customerId)
//	              .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
//	                      "Sorry, we couldn't find a customer with ID: " + customerId));
//	      System.out.println(customer);
//
//	      Agent agent = agentRepository.findById(accountRequestDto.getAgentId())
//	              .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
//	                      "Sorry, we couldn't find an agent with ID: " + accountRequestDto.getAgentId()));
//
//	      // Create a new InsurancePolicy and set its properties
//	      InsurancePolicy insurancePolicy = new InsurancePolicy();
////	      List<Customer> customers = new ArrayList<>();
////	      customers.add(customer);
////	      insurancePolicy.setCustomers(customers); // Set the customer
//	      insurancePolicy.getCustomers().add(customer);
//	      //customer.getInsurancePolicies().add(insurancePolicy);
//	      insurancePolicy.setAgent(agent);
//	      insurancePolicy.setPolicyTerm(accountRequestDto.getPolicyTerm());
//	      insurancePolicy.setPremiumAmount(accountRequestDto.getPremiumAmount());
//	      insurancePolicy.setIssuedDate(LocalDate.now());
//	      insurancePolicy.setMaturityDate(insurancePolicy.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
//	      insurancePolicy.setInstallmentPeriod(accountRequestDto.getInstallmentPeriod());
//	      insurancePolicy.setRegisteredCommission(insuranceScheme.getNewRegistrationCommission());
//	      insurancePolicy.setInsuranceScheme(insuranceScheme);
//	      insurancePolicy.setPolicyStatus(PolicyStatus.ACTIVE.name());
//	      insurancePolicy.setActive(true);
//
//	      // Calculate the sum assured based on the profit ratio
//	      double sumAssured = (insurancePolicy.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
//	              + insurancePolicy.getPremiumAmount();
//	      insurancePolicy.setClaimAmount(sumAssured);
//
//	      // Determine the number of months based on the premium type
//	      long months = accountRequestDto.getInstallmentPeriod();
//
//	      // Calculate the total number of months and the installment amount
//	      long totalMonths = insurancePolicy.getPolicyTerm() * 12 / months;
//	      double installmentAmount = insurancePolicy.getPremiumAmount() / totalMonths;
//	      insurancePolicy.setInstallmentPayment(installmentAmount);
//	      insurancePolicy.setTotalAmountPaid(0.0);
//
//	      // Handle Nominees
//	      if (accountRequestDto.getNominees() != null && !accountRequestDto.getNominees().isEmpty()) {
//	          List<Nominee> nominees = new ArrayList<>();
//	          for (NomineeDto nomineeDto : accountRequestDto.getNominees()) {
//	              Nominee nominee = new Nominee();
//	              nominee.setNomineeName(nomineeDto.getNomineeName());
//	              nominee.setRelationStatus(nomineeDto.getRelationStatus());
//	              nomineeRepository.save(nominee); // Save the new nominee to the repository
//	              nominees.add(nominee);
//	          }
//	          insurancePolicy.setNominees(nominees);
//	      } else {
//	          throw new APIException(HttpStatus.NOT_FOUND, "No nominees provided in the request");
//	      }
//	// Handle Documents
//	      if (accountRequestDto.getDocuments() != null && !accountRequestDto.getDocuments().isEmpty()) {
//	          Set<SubmittedDocument> documents = new HashSet<>();
//	          for (SubmittedDocumentDto documentDto : accountRequestDto.getDocuments()) {
//	              SubmittedDocument document = new SubmittedDocument();
//	              document.setDocumentName(documentDto.getDocumentName());
//	              document.setDocumentStatus(documentDto.getDocumentStatus()); // Setting document status
//	              document.setDocumentImage(documentDto.getDocumentImage()); // Setting document image
//	              documentRepository.save(document); // Save the new document to the repository
//	              documents.add(document);
//	          }
//	          insurancePolicy.setDocuments(documents);
//	      } else {
//	          throw new APIException(HttpStatus.NOT_FOUND, "No documents provided in the request");
//	      }
//
//	      // Save the insurance policy to the repository
//	      insurancePolicyRepository.save(insurancePolicy);
//
//	      // Update the customer's list of policies
//	      customer.getInsurancePolicies().add(insurancePolicy);
//	      customerRepository.save(customer);
//
//	      return "Policy has been successfully created for customer ID " + customerId + ".";
//	  }
	
	@Override
	public String buyPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId) {
		System.out.println("c---------------------------------------------------");
	    if (accountRequestDto.getInsuranceSchemeId() == null) {
	        throw new APIException(HttpStatus.BAD_REQUEST, "Insurance scheme ID must not be null");
	    }

	    InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
	            "Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));

	    Customer customer = customerRepository.findById(customerId)
	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
	            "Sorry, we couldn't find a customer with ID: " + customerId));

	    // Create a new InsurancePolicy and set its properties
	    InsurancePolicy insurancePolicy = new InsurancePolicy();

	    insurancePolicy.getCustomers().add(customer);

	    // Since there's no agent, set a default or null value for agent
	    insurancePolicy.setAgent(null);

	    insurancePolicy.setPolicyTerm(accountRequestDto.getPolicyTerm());
	    insurancePolicy.setPremiumAmount(accountRequestDto.getPremiumAmount());
	    insurancePolicy.setIssuedDate(LocalDate.now()); // Assuming issued date is set to the current date
	    insurancePolicy.setMaturityDate(insurancePolicy.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
	    insurancePolicy.setInstallmentPeriod(accountRequestDto.getInstallmentPeriod());
	    insurancePolicy.setRegisteredCommission(0.0); // No commission for policies bought without an agent
	    insurancePolicy.setInsuranceScheme(insuranceScheme);
	    System.out.println("ppp------");
	    if (accountRequestDto.getNominees() != null && !accountRequestDto.getNominees().isEmpty()) {
	    	System.out.println("checking for nominees---------------------------------------------------");
	        List<Nominee> nominees = new ArrayList<>();
	    //    for (NomineeDto nomineeDto : accountRequestDto.getNominees()) {
	        for(NomineeDto nomineeDto:accountRequestDto.getNominees()) {
	            Nominee nominee = new Nominee();
	            nominee.setNomineeName(nomineeDto.getNomineeName());
	            nominee.setRelationStatus(nomineeDto.getRelationStatus());
	            
	           nominee =nomineeRepository.save(nominee);
	            nominees.add(nominee);
	            
	        	//InsuranceScheme nominee=nomineeRepository.findById(nomineeId).orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"nominee not found withID: "+nomineeId));
	        }
	        insurancePolicy.setNominees(nominees);
	        System.out.println(nominees);// Add nominees to the policy
	    }
		   
	    

	    // Calculate the sum assured based on the profit ratio
	    double sumAssured = (insurancePolicy.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
	        + insurancePolicy.getPremiumAmount();
	    insurancePolicy.setClaimAmount(sumAssured);
	    insurancePolicy.setPolicyStatus(PolicyStatus.ACTIVE.name());

	    // Determine the number of months based on the premium type
	    long months = accountRequestDto.getInstallmentPeriod();
	    // Calculate the total number of months and the installment amount
	    long totalMonths = insurancePolicy.getPolicyTerm() * 12 / months;
	    double installmentAmount = insurancePolicy.getPremiumAmount() / totalMonths;
	    insurancePolicy.setInstallmentPayment(installmentAmount);
	    insurancePolicy.setTotalAmountPaid(0.0);
	    
	 // Handle Documents
	      if (accountRequestDto.getDocuments() != null && !accountRequestDto.getDocuments().isEmpty()) {
	          Set<SubmittedDocument> documents = new HashSet<>();
	          for (SubmittedDocumentDto documentDto : accountRequestDto.getDocuments()) {
	              SubmittedDocument document = new SubmittedDocument();
	              document.setDocumentName(documentDto.getDocumentName());
	              document.setDocumentStatus(documentDto.getDocumentStatus()); // Setting document status
	              document.setDocumentImage(documentDto.getDocumentImage()); // Setting document image
	              documentRepository.save(document); // Save the new document to the repository
	              documents.add(document);
	          }
	          insurancePolicy.setDocuments(documents);
	      } else {
	          throw new APIException(HttpStatus.NOT_FOUND, "No documents provided in the request");
	      }

	      // Save the insurance policy to the repository
	      insurancePolicyRepository.save(insurancePolicy);

	      // Update the customer's list of policies
	      customer.getInsurancePolicies().add(insurancePolicy);
	      customerRepository.save(customer);

	    // Save the insurance policy to the repository
	    insurancePolicyRepository.save(insurancePolicy);

	    return "Policy has been successfully created for customer ID " + customerId + " without an agent.";
	
	 

}

	@Override
	public String claimPolicy(ClaimRequestDto claimRequestDto, long customerId) {
		
	
	        // Fetch the policy by ID
	        InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(claimRequestDto.getPolicyId())
	                .orElseThrow(() -> new RuntimeException("Policy not found"));

	        // Check if the policy has matured
	        if (insurancePolicy.getMaturityDate().isAfter(LocalDate.now())) {
	            throw new RuntimeException("The policy has not yet matured. Claims can only be made after the maturity date.");
	        }

	        // Create a new Claim
	        Claim claim = new Claim();
	        claim.setClaimAmount(insurancePolicy.getClaimAmount()); // Claim amount is the maturity sum
	        claim.setBankName(claimRequestDto.getBankName());
	        claim.setBranchName(claimRequestDto.getBranchName());
	        claim.setBankAccountId(claimRequestDto.getBankAccountId());
	        claim.setIfscCode(claimRequestDto.getIfscCode());
	        claim.setClaimedStatus(ClaimStatus.PENDING.name());
	        claim.setPolicy(insurancePolicy);

	        // Assign the agent handling the policy to the claim
	        claim.setAgent(insurancePolicy.getAgent());

	        // Link the claim to the insurance policy
	        insurancePolicy.setClaim(claim);

	        // Save the claim
	        claimRepository.save(claim);

	        return "Claim has been successfully created for policy ID " + claimRequestDto.getPolicyId();
	}

	@Override
	public String requestPolicyCancellation(CancellationRequestDto cancellationRequest, Long customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	  @Override
	  public String customerCancelPolicy(ClaimRequestDto claimRequestDto, Long customerId) {
	      
		if (claimRequestDto == null) {
	        throw new IllegalArgumentException("ClaimRequestDto cannot be null.");
	    }
	    
	    // Extract policyId and ensure it's not null
	    Long policyId = claimRequestDto.getPolicyId();
	    if (policyId == null) {
	        throw new IllegalArgumentException("Policy ID cannot be null.");
	    }
		
		// Find the policy by its ID
	      InsurancePolicy policy = insurancePolicyRepository.findById(claimRequestDto.getPolicyId())
	              .orElseThrow(() -> new AllExceptions.PolicyNotFoundException("Policy not found"));

	      // Check if the given customer is associated with the policy
	      boolean customerExistsInPolicy = policy.getCustomers().stream()
	              .anyMatch(customer -> customer.getCustomerId() == customerId);

	      if (!customerExistsInPolicy) {
	          throw new AllExceptions.CustomerNotFoundException("Customer is not associated with this policy.");
	      }

	      // Check if a claim already exists for the policy
	      Optional<Claim> existingClaim = claimRepository.findByPolicy(policy);

	      // Log or print for debugging purposes
	      if (existingClaim.isPresent()) {
	          System.out.println("Claim already exists for the policy, updating it...");
	      } else {
	          System.out.println("No existing claim, creating a new one...");
	      }

	      // If a claim exists and the customer wants to cancel, apply the cancellation logic
	      Claim claim;
	      if (existingClaim.isPresent()) {
	          claim = existingClaim.get();
	      } else {
	          // If no claim exists, create a new one
	          claim = new Claim();
	          claim.setPolicy(policy);
	          // Since there's no direct link to the customer, we don't set the customer directly in the claim
	      }

	      // Get the policy amount (assume policy has a premiumAmount or totalAmount field)
	     // double policyAmount = policy.getPremiumAmount();
	      double policyAmount = policy.getTotalAmountPaid();
	      // Calculate the claim amount and apply 20% deduction for cancellation
	      double claimAmount;
	      if (policy.getMaturityDate().isAfter(LocalDate.now())) {
	          // Apply 20% deduction if canceled before maturity
	          double deductionPercentage = Double.parseDouble(keyValueRepository.getValueByKey("deduction_percentage"));
	          double deductionAmount = policyAmount * (deductionPercentage / 100);
	          claimAmount = policyAmount - deductionAmount;

	          // Mark as canceled and set the deduction details
	          claim.setCancel(true);
	          System.out.println("Policy canceled before maturity, applying 20% deduction.");
	      } else {
	          // No deduction if claimed after maturity
	          claimAmount = policyAmount;
	          claim.setCancel(false);  // No cancellation flag after maturity
	          System.out.println("Policy claimed after maturity, no deduction applied.");
	      }

	      // Set claim details from the request
	      claim.setClaimAmount(claimAmount);
	      claim.setBankName(claimRequestDto.getBankName());
	      claim.setBranchName(claimRequestDto.getBranchName());
	      claim.setBankAccountId(claimRequestDto.getBankAccountId());
	      claim.setIfscCode(claimRequestDto.getIfscCode());
	      claim.setClaimedStatus("PENDING");

	      // Save the claim in the repository
	      claimRepository.save(claim);
	      System.out.println("Claim has been saved: " + claim);

	      return "Policy cancellation or claim has been created for the customer.";
	  }
	@Override
	public Page<Customer> getAllCustomers(Pageable pageable) {
	    return customerRepository.findAll(pageable);
	}

}

//package com.techlabs.app.service;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import com.techlabs.app.controller.AdminController;
//import com.techlabs.app.dto.CancellationRequestDto;
//import com.techlabs.app.dto.ChangePasswordDto;
//import com.techlabs.app.dto.ClaimRequestDto;
//import com.techlabs.app.dto.CustomerRequestDto;
//import com.techlabs.app.dto.CustomerResponseDto;
//import com.techlabs.app.dto.InsurancePolicyDto;
//import com.techlabs.app.dto.NomineeDto;
//import com.techlabs.app.dto.PolicyAccountRequestDto;
//import com.techlabs.app.dto.RegisterDto;
//import com.techlabs.app.dto.SubmittedDocumentDto;
//import com.techlabs.app.entity.Agent;
//import com.techlabs.app.entity.City;
//import com.techlabs.app.entity.Claim;
//import com.techlabs.app.entity.ClaimStatus;
//import com.techlabs.app.entity.Customer;
//import com.techlabs.app.entity.DocumentStatus;
//import com.techlabs.app.entity.Employee;
//import com.techlabs.app.entity.InsurancePolicy;
//import com.techlabs.app.entity.InsuranceScheme;
//import com.techlabs.app.entity.Nominee;
//import com.techlabs.app.entity.Payment;
//import com.techlabs.app.entity.PolicyStatus;
//import com.techlabs.app.entity.PremiumType;
//import com.techlabs.app.entity.RelationStatus;
//import com.techlabs.app.entity.Role;
//import com.techlabs.app.entity.SchemeDocument;
//import com.techlabs.app.entity.SubmittedDocument;
//import com.techlabs.app.entity.User;
//import com.techlabs.app.exception.APIException;
//import com.techlabs.app.exception.AllExceptions;
//import com.techlabs.app.exception.BankApiException;
//import com.techlabs.app.exception.CustomerNotFoundException;
//import com.techlabs.app.exception.ResourceNotFoundException;
//import com.techlabs.app.repository.AgentRepository;
//import com.techlabs.app.repository.CityRepository;
//import com.techlabs.app.repository.ClaimRepository;
//import com.techlabs.app.repository.CustomerRepository;
//import com.techlabs.app.repository.DocumentRepository;
//import com.techlabs.app.repository.InsurancePolicyRepository;
//import com.techlabs.app.repository.InsuranceSchemeRepository;
//import com.techlabs.app.repository.KeyValueRepository;
//import com.techlabs.app.repository.NomineeRepository;
//import com.techlabs.app.repository.RoleRepository;
//import com.techlabs.app.repository.SubmittedDocumentRepository;
//import com.techlabs.app.repository.UserRepository;
//import com.techlabs.app.util.PagedResponse;
//
//import jakarta.transaction.Transactional;
//
//@Service
//public class CustomerServiceImpl implements CustomerService {
//
//	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private RoleRepository roleRepository;
//
//	@Autowired
//	private CustomerRepository customerRepository;
//
//	@Autowired
//	private CityRepository cityRepository;
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	@Autowired
//	private AgentRepository agentRepository;
//
//	@Autowired
//	private InsuranceSchemeRepository insuranceSchemeRepository;
//
//	@Autowired
//	private InsurancePolicyRepository insurancePolicyRepository;
//
//	@Autowired
//	private NomineeRepository nomineeRepository;
//
//	@Autowired
//	private ClaimRepository claimRepository;
//
//	@Autowired
//	private DocumentRepository documentRepository;
//
//	@Autowired
//	private KeyValueRepository keyValueRepository;
//
//	@Autowired
//	private SubmittedDocumentRepository submittedDocumentRepository;
//
//	public CustomerServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
//			CustomerRepository customerRepository, CityRepository cityRepository, PasswordEncoder passwordEncoder,
//			AgentRepository agentRepository, InsuranceSchemeRepository insuranceSchemeRepository,
//			InsurancePolicyRepository insurancePolicyRepository, NomineeRepository nomineeRepository,
//			ClaimRepository claimRepository) {
//		super();
//		this.userRepository = userRepository;
//		this.roleRepository = roleRepository;
//		this.customerRepository = customerRepository;
//		this.cityRepository = cityRepository;
//		this.passwordEncoder = passwordEncoder;
//		this.agentRepository = agentRepository;
//		this.insuranceSchemeRepository = insuranceSchemeRepository;
//		this.insurancePolicyRepository = insurancePolicyRepository;
//		this.nomineeRepository = nomineeRepository;
//		this.claimRepository = claimRepository;
//	}
//
//	@Override
//	@Transactional
//
//	public void addCustomer(RegisterDto registerDto) {
//		// Check if the username or email already exists
//		if (userRepository.existsByUsername(registerDto.getUsername())) {
//			throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
//		}
//
//		if (userRepository.existsByEmail(registerDto.getEmail())) {
//			throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
//		}
//
//		// Create a new user
//		User user = new User();
//		user.setUsername(registerDto.getUsername());
//		user.setEmail(registerDto.getEmail());
//		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//
//		// Assign customer role to the user
//		Set<Role> roles = new HashSet<>();
//		Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
//				.orElseThrow(() -> new BankApiException(HttpStatus.BAD_REQUEST, "Customer role not found"));
//		roles.add(customerRole);
//		user.setRoles(roles);
//
//		// Save user to the repository
//		userRepository.save(user);
//
//		// Find the city using cityId
//		City city = cityRepository.findById(registerDto.getCityId())
//				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "City not found"));
//
//		// Create a new customer
//		Customer customer = new Customer();
//		customer.setUser(user);
//		customer.setFirstName(registerDto.getFirstName());
//		customer.setLastName(registerDto.getLastName());
//		customer.setPhoneNumber(registerDto.getPhone_number());
//		customer.setDob(registerDto.getDob());
//		customer.setCity(city);
//		customer.setActive(true); // Set default status as active
//		customer.setVerified(false); // Set default verification status
//
//		// Save customer to the repository
//		customerRepository.save(customer);
//	}
//
//	public PagedResponse<CustomerResponseDto> getAllCustomers(int page, int size) {
//		Pageable pageable = PageRequest.of(page, size);
//		Page<Customer> customerPage = customerRepository.findAll(pageable);
//
//		List<CustomerResponseDto> customerResponseDtos = customerPage.getContent().stream().map(this::convertToDto)
//				.collect(Collectors.toList());
//
//		return new PagedResponse<>(customerResponseDtos, customerPage.getNumber(), customerPage.getSize(),
//				customerPage.getTotalElements(), customerPage.getTotalPages(), customerPage.isLast());
//	}
//
//	private CustomerResponseDto convertToDto(Customer customer) {
//		CustomerResponseDto dto = new CustomerResponseDto();
//		BeanUtils.copyProperties(customer, dto);
//		if (customer.getCity() != null) {
//			dto.setCityName(customer.getCity().getCity_name()); // Adjust according to your City entity
//		}
//		return dto;
//	}
//
////	@Override
////	public String createInsurancePolicy(InsurancePolicyDto insurancePolicyDto) {
////		// TODO Auto-generated method stub
////		return null;
////	}
////	
//
//	@Override
//	public String createInsurancePolicy(InsurancePolicyDto insurancePolicyDto) {
//		// Create a new InsurancePolicy entity
//		InsurancePolicy policy = new InsurancePolicy();
//
//		// Map basic fields from the DTO to the entity
//		policy.setIssuedDate(insurancePolicyDto.getIssuedDate());
//		policy.setMaturityDate(insurancePolicyDto.getMaturityDate());
//		policy.setPremiumAmount(insurancePolicyDto.getPremiumAmount());
//		policy.setPolicyStatus(insurancePolicyDto.getPolicyStatus());
//		// policy.setActive(insurancePolicyDto.isActive());
//
//		// Fetch and set associated InsuranceScheme entity
//		InsuranceScheme scheme = insuranceSchemeRepository.findById(insurancePolicyDto.getInsuranceSchemeId())
//				.orElseThrow(() -> new RuntimeException(
//						"Insurance Scheme not found for ID: " + insurancePolicyDto.getInsuranceSchemeId()));
//		policy.setInsuranceScheme(scheme);
//
//		// Fetch and set associated Agent entity
//		Agent agent = agentRepository.findById(insurancePolicyDto.getAgentId())
//				.orElseThrow(() -> new RuntimeException("Agent not found for ID: " + insurancePolicyDto.getAgentId()));
//		policy.setAgent(agent);
//
//		insurancePolicyRepository.save(policy);
//
//		return "Insurance Policy created successfully.";
//	}
//
//	@Override
//	@Transactional
//	public String buyPolicy(InsurancePolicyDto accountRequestDto, long customerId) {
//
//		// Log the customer ID
//		System.out.println("Customer ID received: " + customerId);
//
//		// Fetch the required entities (InsuranceScheme, Customer, Agent)
//		if (accountRequestDto.getInsuranceSchemeId() == null) {
//			throw new IllegalArgumentException("Insurance Scheme ID must not be null");
//		}
//
//		InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
//				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
//						"Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));
//		System.out.println("Insurance Scheme fetched: " + insuranceScheme);
//		// Check if the insurance scheme is active
//		if (!insuranceScheme.isActive()) {
//			throw new APIException(HttpStatus.FORBIDDEN, "The insurance scheme with ID "
//					+ accountRequestDto.getInsuranceSchemeId() + " is not active and cannot be used to buy a policy.");
//		}
//		System.out.println("Insurance Scheme is active");
//		System.out.println("customer checkinh");
//		Customer customer = customerRepository.findById(customerId)
//				.orElseThrow(() -> new AllExceptions.IdNotFoundException(
//						"Sorry, we couldn't find a customer with ID: " + customerId));
//		System.out.println("Customer fetched: " + customer);
//
//		if (customer == null) {
//			throw new IllegalArgumentException("Customer ID must not be null");
//		}
//
//		// Calculate the customer's age
//		int customerAge = LocalDate.now().getYear() - customer.getDob().getYear();
//		if (customer.getDob().plusYears(customerAge).isAfter(LocalDate.now())) {
//			customerAge--; // Adjust age if the birthday hasn't occurred yet this year
//		}
//
//		// Check customer's age against the scheme's age limits
//		if (customerAge < insuranceScheme.getMinimumAge() || customerAge > insuranceScheme.getMaximumAge()) {
//			throw new APIException(HttpStatus.FORBIDDEN, "Customer with ID " + customerId
//					+ " does not meet the age requirements for the selected insurance scheme.");
//		}
//
//		// Check if the customer is verified
//		if (!customer.isVerified()) {
//			throw new APIException(HttpStatus.FORBIDDEN,
//					"Customer with ID " + customerId + " is not verified and cannot purchase a policy.");
//		}
//
//		Agent agent = agentRepository.findById(accountRequestDto.getAgentId())
//				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
//						"Sorry, we couldn't find an agent with ID: " + accountRequestDto.getAgentId()));
//
//		// Create a new InsurancePolicy and set its properties
//		InsurancePolicy insurancePolicy = new InsurancePolicy();
//		insurancePolicy.getCustomers().add(customer); // Set the customer
//		insurancePolicy.setAgent(agent);
//		insurancePolicy.setPolicyTerm(accountRequestDto.getPolicyTerm());
//		insurancePolicy.setPremiumAmount(accountRequestDto.getPremiumAmount());
//		insurancePolicy.setIssuedDate(LocalDate.now());
//		insurancePolicy.setMaturityDate(insurancePolicy.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
//		insurancePolicy.setInstallmentPeriod(accountRequestDto.getInstallmentPeriod());
//		insurancePolicy.setRegisteredCommission(insuranceScheme.getNewRegistrationCommission());
//		insurancePolicy.setInsuranceScheme(insuranceScheme);
//		insurancePolicy.setPolicyStatus(PolicyStatus.PENDING.name());
//
//		// Calculate the sum assured based on the profit ratio
//		double sumAssured = (insurancePolicy.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
//				+ insurancePolicy.getPremiumAmount();
//		insurancePolicy.setClaimAmount(sumAssured);
//
//		// Determine the number of months based on the premium type
//		long months = accountRequestDto.getInstallmentPeriod();
//		long totalMonths = insurancePolicy.getPolicyTerm() * 12 / months;
//		double installmentAmount = insurancePolicy.getPremiumAmount() / totalMonths;
//		insurancePolicy.setInstallmentPayment(installmentAmount);
//		insurancePolicy.setTotalAmountPaid(0.0);
//
//		// Handle Nominees
//		if (accountRequestDto.getNominees() != null && !accountRequestDto.getNominees().isEmpty()) {
//			List<Nominee> nominees = new ArrayList<>();
//			for (NomineeDto nomineeDto : accountRequestDto.getNominees()) {
//				Nominee nominee = new Nominee();
//				nominee.setNomineeName(nomineeDto.getNomineeName());
//				// nominee.setRelationStatus(nomineeDto.getRelationStatus());
//				nomineeRepository.save(nominee); // Save the nominee
//				nominees.add(nominee);
//			}
//			insurancePolicy.setNominees(nominees);
//		} else {
//			throw new APIException(HttpStatus.NOT_FOUND, "No nominees provided in the request");
//		}
//
//		// Handle Submitted Documents based on Scheme Documents
//		Set<SchemeDocument> schemeDocuments = insuranceScheme.getSchemeDocuments();
//		if (schemeDocuments != null && !schemeDocuments.isEmpty()) {
//			Set<SubmittedDocument> submittedDocuments = new HashSet<>();
//			for (SchemeDocument schemeDoc : schemeDocuments) {
//				boolean documentFound = false;
//
//				// Iterate through submitted documents to find a match based on document name
//				for (SubmittedDocumentDto submittedDto : accountRequestDto.getDocuments()) {
//					if (schemeDoc.getName().equalsIgnoreCase(submittedDto.getDocumentName())) {
//						SubmittedDocument submittedDoc = new SubmittedDocument();
//						submittedDoc.setDocumentName(schemeDoc.getName());
//						submittedDoc.setDocumentStatus(DocumentStatus.PENDING.name()); // Default status
//						submittedDoc.setDocumentImage(submittedDto.getDocumentImage()); // Set the document image from
//						// DTOdocumentRepository.save(submittedDoc); // Save the submitted document
//
//						documentRepository.save(submittedDoc);
//						submittedDocuments.add(submittedDoc);
//						documentFound = true;
//						break;
//					}
//				}
//
//				// If no matching submitted document is found for a required scheme document,
//				// throw an exception
//				if (!documentFound) {
//					throw new APIException(HttpStatus.BAD_REQUEST,
//							"Document for " + schemeDoc.getName() + " is missing.");
//				}
//			}
//			insurancePolicy.setDocuments(submittedDocuments);
//		} else {
//			throw new APIException(HttpStatus.NOT_FOUND, "No scheme documents available for the selected scheme.");
//		}
//
//		// Save the insurance policy
//		insurancePolicyRepository.save(insurancePolicy);
//
//		// Update the customer's list of policies
//		customer.getInsurancePolicies().add(insurancePolicy);
//		customerRepository.save(customer);
//
//		return "Policy has been successfully created for customer ID " + customerId + ".";
//	}
//
////
//// ---------------------------------
////	@Override
////	public String buyPolicy(InsurancePolicyDto accountRequestDto, long customerId) {
////		
////		if (accountRequestDto.getInsuranceSchemeId() == null) {
////	        throw new APIException(HttpStatus.BAD_REQUEST, "Insurance scheme ID must not be null");
////	    }
////
////	    InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
////	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
////	            "Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));
////
////	    Customer customer = customerRepository.findById(customerId)
////	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
////	            "Sorry, we couldn't find a customer with ID: " + customerId));
////
////	    Agent agent = agentRepository.findById(accountRequestDto.getAgentId())
////	        .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
////	            "Sorry, we couldn't find an agent with ID: " + accountRequestDto.getAgentId()));
////
////	    // Create a new InsurancePolicy and set its properties
////	    InsurancePolicy insurancePolicy = new InsurancePolicy();
////
////	    insurancePolicy.getCustomers().add(customer);
////	    
////	    insurancePolicy.setAgent(agent);
////
////	    insurancePolicy.setPolicyTerm(accountRequestDto.getPolicyTerm());
////	    insurancePolicy.setPremiumAmount(accountRequestDto.getPremiumAmount());
////	    insurancePolicy.setIssuedDate(LocalDate.now()); // Assuming issued date is set to the current date
////	    insurancePolicy.setMaturityDate(insurancePolicy.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
////	    insurancePolicy.setInstallmentPeriod(accountRequestDto.getInstallmentPeriod());
////	    insurancePolicy.setRegisteredCommission(insuranceScheme.getNewRegistrationCommission());
////	    insurancePolicy.setInsuranceScheme(insuranceScheme);
////	    insurancePolicy.setActive(true);
////	    insurancePolicy.setPolicyStatus(PolicyStatus.PENDING.name());
////	    
////	    
////	    //handle nominees
////	    if (accountRequestDto.getNominees() != null && !accountRequestDto.getNominees().isEmpty()) {
////	    	System.out.println("checking for nominees---------------------------------------------------");
////	        List<Nominee> nominees = new ArrayList<>();
////	    //    for (NomineeDto nomineeDto : accountRequestDto.getNominees()) {
////	        for(NomineeDto nomineeDto:accountRequestDto.getNominees()) {
////	            Nominee nominee = new Nominee();
////	            nominee.setNomineeName(nomineeDto.getNomineeName());
////	            nominee.setRelationStatus(nomineeDto.getRelationStatus());
////	            
////	           nominee =nomineeRepository.save(nominee);
////	            nominees.add(nominee);
////	            
////	        	//InsuranceScheme nominee=nomineeRepository.findById(nomineeId).orElseThrow(()->new APIException(HttpStatus.NOT_FOUND,"nominee not found withID: "+nomineeId));
////	        }
////	        insurancePolicy.setNominees(nominees);
////	        System.out.println(nominees);// Add nominees to the policy
////	    }
////	    
////	    double totalCommission = agent.getTotalCommission() + insurancePolicy.getRegisteredCommission();
////	    agent.setTotalCommission(totalCommission);
////	    
////	    // Calculate the sum assured based on the profit ratio
////	    double sumAssured = (insurancePolicy.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
////	        + insurancePolicy.getPremiumAmount();
////	    insurancePolicy.setClaimAmount(sumAssured);
////	    insurancePolicy.setPolicyStatus(PolicyStatus.ACTIVE.name());
////
////	    // Determine the number of months based on the premium type
////	    long months= accountRequestDto.getInstallmentPeriod();
////	// Calculate the total number of months and the installment amount
////	    long totalMonths = insurancePolicy.getPolicyTerm() * 12 / months;
////	    double installmentAmount = insurancePolicy.getPremiumAmount() / totalMonths;
////	    insurancePolicy.setInstallmentPayment(installmentAmount);
////	    insurancePolicy.setTotalAmountPaid(0.0);
////	    
////	 // Handle Documents
////	      if (accountRequestDto.getDocuments() != null && !accountRequestDto.getDocuments().isEmpty()) {
////	          Set<SubmittedDocument> documents = new HashSet<>();
////	          for (SubmittedDocumentDto documentDto : accountRequestDto.getDocuments()) {
////	              SubmittedDocument document = new SubmittedDocument();
////	              document.setDocumentName(documentDto.getDocumentName());
////	              document.setDocumentStatus(documentDto.getDocumentStatus()); // Setting document status
////	              document.setDocumentImage(documentDto.getDocumentImage()); // Setting document image
////	              documentRepository.save(document); // Save the new document to the repository
////	              documents.add(document);
////	          }
////	          insurancePolicy.setDocuments(documents);
////	      } else {
////	          throw new APIException(HttpStatus.NOT_FOUND, "No documents provided in the request");
////	      }
////
////	      // Save the insurance policy to the repository
////	      insurancePolicyRepository.save(insurancePolicy);
////
////	      // Update the customer's list of policies
////	      customer.getInsurancePolicies().add(insurancePolicy);
////	      customerRepository.save(customer);
////
////	    // Save the insurance policy to the repository
////	    insurancePolicyRepository.save(insurancePolicy);
////
////	    return "Policy has been successfully created for customer ID " + customerId + ".";
////	  }
//// --------------------------
////	@Override
////	  @Transactional
////	  public String buyPolicy(InsurancePolicyDto accountRequestDto, long customerId) {
////	      // Fetch the required entities (InsuranceScheme, Customer, Agent)
////	      InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
////	              .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
////	                      "Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));
////
////	      Customer customer = customerRepository.findById(customerId)
////	              .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
////	                      "Sorry, we couldn't find a customer with ID: " + customerId));
////	      System.out.println(customer);
////
////	      Agent agent = agentRepository.findById(accountRequestDto.getAgentId())
////	              .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
////	                      "Sorry, we couldn't find an agent with ID: " + accountRequestDto.getAgentId()));
////
////	      // Create a new InsurancePolicy and set its properties
////	      InsurancePolicy insurancePolicy = new InsurancePolicy();
//////	      List<Customer> customers = new ArrayList<>();
//////	      customers.add(customer);
//////	      insurancePolicy.setCustomers(customers); // Set the customer
////	      insurancePolicy.getCustomers().add(customer);
////	      //customer.getInsurancePolicies().add(insurancePolicy);
////	      insurancePolicy.setAgent(agent);
////	      insurancePolicy.setPolicyTerm(accountRequestDto.getPolicyTerm());
////	      insurancePolicy.setPremiumAmount(accountRequestDto.getPremiumAmount());
////	      insurancePolicy.setIssuedDate(LocalDate.now());
////	      insurancePolicy.setMaturityDate(insurancePolicy.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
////	      insurancePolicy.setInstallmentPeriod(accountRequestDto.getInstallmentPeriod());
////	      insurancePolicy.setRegisteredCommission(insuranceScheme.getNewRegistrationCommission());
////	      insurancePolicy.setInsuranceScheme(insuranceScheme);
////	      insurancePolicy.setPolicyStatus(PolicyStatus.ACTIVE.name());
////	      insurancePolicy.setActive(true);
////
////	      // Calculate the sum assured based on the profit ratio
////	      double sumAssured = (insurancePolicy.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
////	              + insurancePolicy.getPremiumAmount();
////	      insurancePolicy.setClaimAmount(sumAssured);
////
////	      // Determine the number of months based on the premium type
////	      long months = accountRequestDto.getInstallmentPeriod();
////
////	      // Calculate the total number of months and the installment amount
////	      long totalMonths = insurancePolicy.getPolicyTerm() * 12 / months;
////	      double installmentAmount = insurancePolicy.getPremiumAmount() / totalMonths;
////	      insurancePolicy.setInstallmentPayment(installmentAmount);
////	      insurancePolicy.setTotalAmountPaid(0.0);
////
////	      // Handle Nominees
////	      if (accountRequestDto.getNominees() != null && !accountRequestDto.getNominees().isEmpty()) {
////	          List<Nominee> nominees = new ArrayList<>();
////	          for (NomineeDto nomineeDto : accountRequestDto.getNominees()) {
////	              Nominee nominee = new Nominee();
////	              nominee.setNomineeName(nomineeDto.getNomineeName());
////	              nominee.setRelationStatus(nomineeDto.getRelationStatus());
////	              nomineeRepository.save(nominee); // Save the new nominee to the repository
////	              nominees.add(nominee);
////	          }
////	          insurancePolicy.setNominees(nominees);
////	      } else {
////	          throw new APIException(HttpStatus.NOT_FOUND, "No nominees provided in the request");
////	      }
////	// Handle Documents
////	      if (accountRequestDto.getDocuments() != null && !accountRequestDto.getDocuments().isEmpty()) {
////	          Set<SubmittedDocument> documents = new HashSet<>();
////	          for (SubmittedDocumentDto documentDto : accountRequestDto.getDocuments()) {
////	              SubmittedDocument document = new SubmittedDocument();
////	              document.setDocumentName(documentDto.getDocumentName());
////	              document.setDocumentStatus(documentDto.getDocumentStatus()); // Setting document status
////	              document.setDocumentImage(documentDto.getDocumentImage()); // Setting document image
////	              documentRepository.save(document); // Save the new document to the repository
////	              documents.add(document);
////	          }
////	          insurancePolicy.setDocuments(documents);
////	      } else {
////	          throw new APIException(HttpStatus.NOT_FOUND, "No documents provided in the request");
////	      }
////
////	      // Save the insurance policy to the repository
////	      insurancePolicyRepository.save(insurancePolicy);
////
////	      // Update the customer's list of policies
////	      customer.getInsurancePolicies().add(insurancePolicy);
////	      customerRepository.save(customer);
////
////	      return "Policy has been successfully created for customer ID " + customerId + ".";
////	  }
//	// ----------- ciorect but
////	@Override
////	  @Transactional
////	  public String buyPolicy(InsurancePolicyDto accountRequestDto, Long customerId) {
////		logger.info("starting buying policy");
////		// Log the start of the method
////	    logger.info("Starting buyPolicy method for customer ID: {}", customerId);
////	    
////	    // Fetch entities
////	    InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
////	        .orElseThrow(() -> {
////	            logger.error("Insurance scheme not found with ID: {}", accountRequestDto.getInsuranceSchemeId());
////	            return new APIException(HttpStatus.NOT_FOUND, "Scheme not found with ID: " + accountRequestDto.getInsuranceSchemeId());
////	        });
////
////	    Customer customer = customerRepository.findById(customerId)
////	        .orElseThrow(() -> {
////	            logger.error("Customer not found with ID: {}", customerId);
////	            return new APIException(HttpStatus.NOT_FOUND, "Customer not found with ID: " + customerId);
////	        });
////
////	    Agent agent = agentRepository.findById(accountRequestDto.getAgentId())
////	        .orElseThrow(() -> {
////	            logger.error("Agent not found with ID: {}", accountRequestDto.getAgentId());
////	            return new APIException(HttpStatus.NOT_FOUND, "Agent not found with ID: " + accountRequestDto.getAgentId());
////	        });
////
////	    // Create a new InsurancePolicy and set its properties
////	    InsurancePolicy insurancePolicy = new InsurancePolicy();
////	    insurancePolicy.getCustomers().add(customer); // Set the customer
////	    insurancePolicy.setAgent(agent);
////	    insurancePolicy.setPolicyTerm(accountRequestDto.getPolicyTerm());
////	    insurancePolicy.setPremiumAmount(accountRequestDto.getPremiumAmount());
////	    insurancePolicy.setIssuedDate(LocalDate.now());
////	    insurancePolicy.setMaturityDate(insurancePolicy.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
////	    insurancePolicy.setInstallmentPeriod(accountRequestDto.getInstallmentPeriod());
////	    insurancePolicy.setRegisteredCommission(insuranceScheme.getNewRegistrationCommission());
////	    insurancePolicy.setInsuranceScheme(insuranceScheme);
////	    insurancePolicy.setPolicyStatus(PolicyStatus.PENDING.name());
////
////	    // Calculate the sum assured based on the profit ratio
////	    double sumAssured = (insurancePolicy.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
////	        + insurancePolicy.getPremiumAmount();
////	    insurancePolicy.setClaimAmount(sumAssured);
////
////	    // Determine the number of months based on the premium type
////	    long months = accountRequestDto.getInstallmentPeriod();
////	    long totalMonths = insurancePolicy.getPolicyTerm() * 12 / months;
////	    double installmentAmount = insurancePolicy.getPremiumAmount() / totalMonths;
////	    insurancePolicy.setInstallmentPayment(installmentAmount);
////	    insurancePolicy.setTotalAmountPaid(0.0);
////
////	    // Handle Nominees
////	    if (accountRequestDto.getNominees() != null && !accountRequestDto.getNominees().isEmpty()) {
////	      List<Nominee> nominees = new ArrayList<>();
////	      for (NomineeDto nomineeDto : accountRequestDto.getNominees()) {
////	        Nominee nominee = new Nominee();
////	        nominee.setNomineeName(nomineeDto.getNomineeName());
////	        nominee.setRelationStatus(nomineeDto.getRelationStatus());
////	        nomineeRepository.save(nominee); // Save the nominee
////	        nominees.add(nominee);
////	      }
////	      insurancePolicy.setNominees(nominees);
////	    } else {
////	      throw new APIException(HttpStatus.NOT_FOUND, "No nominees provided in the request");
////	    }
////
////	    // Handle Submitted Documents based on Scheme Documents
////	    Set<SchemeDocument> schemeDocuments = insuranceScheme.getSchemeDocuments();
////	    if (schemeDocuments != null && !schemeDocuments.isEmpty()) {
////	      Set<SubmittedDocument> submittedDocuments = new HashSet<>();
////	      for (SchemeDocument schemeDoc : schemeDocuments) {
////	        boolean documentFound = false;
////
////	        // Iterate through submitted documents to find a match based on document name
////	        for (SubmittedDocumentDto submittedDto : accountRequestDto.getDocuments()) {
////	          if (schemeDoc.getName().equalsIgnoreCase(submittedDto.getDocumentName())) {
////	            SubmittedDocument submittedDoc = new SubmittedDocument();
////	            submittedDoc.setDocumentName(schemeDoc.getName());
////	            submittedDoc.setDocumentStatus(DocumentStatus.PENDING.name()); // Default status
////	            submittedDoc.setDocumentImage(submittedDto.getDocumentImage()); // Set the document image from
////	                                            // DTOdocumentRepository.save(submittedDoc); // Save the submitted document
////	         //   submittedDoc.setCustomer(customer); // Set the customer ID
////	            submittedDocuments.add(submittedDoc);
////	            documentFound = true;
////	            break;
////	          }
////	        }
//////	        // Check if all documents are verified
//////	        for (SubmittedDocumentDto docDto : accountRequestDto.getDocuments()) {
//////	            SubmittedDocument document = submittedDocumentRepository.findByCustomerIdAndName(customerId, docDto.getDocumentName())
//////	                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Document not found: " + docDto.getDocumentName()));
//////	            if (!DocumentStatus.APPROVED.name().equals(document.getDocumentStatus())) {
//////	                throw new APIException(HttpStatus.BAD_REQUEST, "Document not verified: " + docDto.getDocumentName());
//////	            }
//////	        }
////	        // If no matching submitted document is found for a required scheme document,
////	        // throw an exception
////	        if (!documentFound) {
////	          throw new APIException(HttpStatus.BAD_REQUEST,
////	              "Document for " + schemeDoc.getName() + " is missing.");
////	        }
////	      }
////	      insurancePolicy.setDocuments(submittedDocuments);
////	    } else {
////	      throw new APIException(HttpStatus.NOT_FOUND, "No scheme documents available for the selected scheme.");
////	    }
////
////	    // Save the insurance policy
////	    insurancePolicyRepository.save(insurancePolicy);
////
////	    // Update the customer's list of policies
////	    customer.getInsurancePolicies().add(insurancePolicy);
////	    customerRepository.save(customer);
////
////	    return "Policy has been successfully created for customer ID " + customerId + ".";
////	  }
//	// to here--------
//	@Override
//	public String buyPolicyWithoutAgent(InsurancePolicyDto accountRequestDto, long customerId) {
//		System.out.println("c---------------------------------------------------");
//		if (accountRequestDto.getInsuranceSchemeId() == null) {
//			throw new APIException(HttpStatus.BAD_REQUEST, "Insurance scheme ID must not be null");
//		}
//
//		InsuranceScheme insuranceScheme = insuranceSchemeRepository.findById(accountRequestDto.getInsuranceSchemeId())
//				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
//						"Sorry, we couldn't find a scheme with ID: " + accountRequestDto.getInsuranceSchemeId()));
//
//		Customer customer = customerRepository.findById(customerId)
//				.orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND,
//						"Sorry, we couldn't find a customer with ID: " + customerId));
//
//		// Create a new InsurancePolicy and set its properties
//		InsurancePolicy insurancePolicy = new InsurancePolicy();
//
//		insurancePolicy.getCustomers().add(customer);
//
//		// Since there's no agent, set a default or null value for agent
//		insurancePolicy.setAgent(null);
//
//		insurancePolicy.setPolicyTerm(accountRequestDto.getPolicyTerm());
//		insurancePolicy.setPremiumAmount(accountRequestDto.getPremiumAmount());
//		insurancePolicy.setIssuedDate(LocalDate.now()); // Assuming issued date is set to the current date
//		insurancePolicy.setMaturityDate(insurancePolicy.getIssuedDate().plusYears(accountRequestDto.getPolicyTerm()));
//		insurancePolicy.setInstallmentPeriod(accountRequestDto.getInstallmentPeriod());
//		insurancePolicy.setRegisteredCommission(insuranceScheme.getNewRegistrationCommission()); // No commission for
//																									// policies bought
//																									// without an agent
//		insurancePolicy.setInsuranceScheme(insuranceScheme);
//		insurancePolicy.setPolicyStatus(PolicyStatus.PENDING.name());
//
//		// Calculate the sum assured based on the profit ratio
//		double sumAssured = (insurancePolicy.getPremiumAmount() * (insuranceScheme.getProfitRatio() / 100))
//				+ insurancePolicy.getPremiumAmount();
//		insurancePolicy.setClaimAmount(sumAssured);
//		insurancePolicy.setPolicyStatus(PolicyStatus.ACTIVE.name());
//
//		// Determine the number of months based on the premium type
//		long months = accountRequestDto.getInstallmentPeriod();
//		// Calculate the total number of months and the installment amount
//		long totalMonths = insurancePolicy.getPolicyTerm() * 12 / months;
//		double installmentAmount = insurancePolicy.getPremiumAmount() / totalMonths;
//		insurancePolicy.setInstallmentPayment(installmentAmount);
//		insurancePolicy.setTotalAmountPaid(0.0);
//
//		System.out.println("ppp------");
//		// Handle Nominees
//		if (accountRequestDto.getNominees() != null && !accountRequestDto.getNominees().isEmpty()) {
//			List<Nominee> nominees = new ArrayList<>();
//			for (NomineeDto nomineeDto : accountRequestDto.getNominees()) {
//				Nominee nominee = new Nominee();
//				nominee.setNomineeName(nomineeDto.getNomineeName());
//				// if(nomineeDto.getRelationStatus().equals(RelationStatus.PARENT)) {
//				nominee.setRelationStatus(nomineeDto.getRelationStatus());
//				// }
//
//				nomineeRepository.save(nominee); // Save the nominee
//				nominees.add(nominee);
//			}
//			insurancePolicy.setNominees(nominees);
//		} else {
//			throw new APIException(HttpStatus.NOT_FOUND, "No nominees provided in the request");
//		}
//
//		// Handle Submitted Documents based on Scheme Documents
//		Set<SchemeDocument> schemeDocuments = insuranceScheme.getSchemeDocuments();
//		if (schemeDocuments != null && !schemeDocuments.isEmpty()) {
//			Set<SubmittedDocument> submittedDocuments = new HashSet<>();
//			for (SchemeDocument schemeDoc : schemeDocuments) {
//				boolean documentFound = false;
//
//				// Iterate through submitted documents to find a match based on document name
//				for (SubmittedDocumentDto submittedDto : accountRequestDto.getDocuments()) {
//					if (schemeDoc.getName().equalsIgnoreCase(submittedDto.getDocumentName())) {
//						SubmittedDocument submittedDoc = new SubmittedDocument();
//						submittedDoc.setDocumentName(schemeDoc.getName());
//						submittedDoc.setDocumentStatus(DocumentStatus.PENDING.name()); // Default status
//						submittedDoc.setDocumentImage(submittedDto.getDocumentImage()); // Set the document image from
//						// DTOdocumentRepository.save(submittedDoc); // Save the submitted document
//
//						documentRepository.save(submittedDoc);
//						submittedDocuments.add(submittedDoc);
//						documentFound = true;
//						break;
//					}
//				}
//
//				// If no matching submitted document is found for a required scheme document,
//				// throw an exception
//				if (!documentFound) {
//					throw new APIException(HttpStatus.BAD_REQUEST,
//							"Document for " + schemeDoc.getName() + " is missing.");
//				}
//			}
//			insurancePolicy.setDocuments(submittedDocuments);
//		} else {
//			throw new APIException(HttpStatus.NOT_FOUND, "No scheme documents available for the selected scheme.");
//		}
//
//		// Save the insurance policy
//		insurancePolicyRepository.save(insurancePolicy);
//
//		// Update the customer's list of policies
//		customer.getInsurancePolicies().add(insurancePolicy);
//		customerRepository.save(customer);
//
//		return "Policy has been successfully created for customer ID " + customerId + ".";
//	}
//
////	      // Save the insurance policy to the repository
////	      insurancePolicyRepository.save(insurancePolicy);
////
////	      // Update the customer's list of policies
////	      customer.getInsurancePolicies().add(insurancePolicy);
////	      customerRepository.save(customer);
////
////	    // Save the insurance policy to the repository
////	    insurancePolicyRepository.save(insurancePolicy);
////
////	    return "Policy has been successfully created for customer ID " + customerId + " without an agent.";
////	
////	 
////
////}
//
//	@Override
//	public String claimPolicy(ClaimRequestDto claimRequestDto, long customerId) {
//
//		// Fetch the policy by ID
//		InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(claimRequestDto.getPolicyId())
//				.orElseThrow(() -> new RuntimeException("Policy not found"));
//
//		// Check if the policy has matured
//		if (insurancePolicy.getMaturityDate().isAfter(LocalDate.now())) {
//			throw new RuntimeException(
//					"The policy has not yet matured. Claims can only be made after the maturity date.");
//		}
//
//		// Create a new Claim
//		Claim claim = new Claim();
//		claim.setClaimAmount(insurancePolicy.getClaimAmount()); // Claim amount is the maturity sum
//		claim.setBankName(claimRequestDto.getBankName());
//		claim.setBranchName(claimRequestDto.getBranchName());
//		claim.setBankAccountId(claimRequestDto.getBankAccountId());
//		claim.setIfscCode(claimRequestDto.getIfscCode());
//		claim.setClaimedStatus(ClaimStatus.PENDING.name());
//		claim.setPolicy(insurancePolicy);
//
//		// Assign the agent handling the policy to the claim
//		claim.setAgent(insurancePolicy.getAgent());
//
//		// Link the claim to the insurance policy
//		insurancePolicy.setClaim(claim);
//
//		// Save the claim
//		claimRepository.save(claim);
//
//		return "Claim has been successfully created for policy ID " + claimRequestDto.getPolicyId();
//	}
//
//	@Override
//	public String requestPolicyCancellation(CancellationRequestDto cancellationRequest, Long customerId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Transactional
//	@Override
//	public String customerCancelPolicy(ClaimRequestDto claimRequestDto, Long customerId) {
//
//		if (claimRequestDto == null) {
//			throw new IllegalArgumentException("ClaimRequestDto cannot be null.");
//		}
//
//		// Extract policyId and ensure it's not null
//		Long policyId = claimRequestDto.getPolicyId();
//		if (policyId == null) {
//			throw new IllegalArgumentException("Policy ID cannot be null.");
//		}
//
//		// Find the policy by its ID
//		InsurancePolicy policy = insurancePolicyRepository.findById(claimRequestDto.getPolicyId())
//				.orElseThrow(() -> new AllExceptions.PolicyNotFoundException("Policy not found"));
//
//		// Check if the given customer is associated with the policy
//		boolean customerExistsInPolicy = policy.getCustomers().stream()
//				.anyMatch(customer -> customer.getCustomerId() == customerId);
//
//		if (!customerExistsInPolicy) {
//			throw new AllExceptions.CustomerNotFoundException("Customer is not associated with this policy.");
//		}
//
//		// Check if a claim already exists for the policy
//		Optional<Claim> existingClaim = claimRepository.findByPolicy(policy);
//
//		// Log or print for debugging purposes
//		if (existingClaim.isPresent()) {
//			System.out.println("Claim already exists for the policy, updating it...");
//		} else {
//			System.out.println("No existing claim, creating a new one...");
//		}
//
//		// If a claim exists and the customer wants to cancel, apply the cancellation
//		// logic
//		Claim claim;
//		if (existingClaim.isPresent()) {
//			claim = existingClaim.get();
//		} else {
//			// If no claim exists, create a new one
//			claim = new Claim();
//			claim.setPolicy(policy);
//			// Since there's no direct link to the customer, we don't set the customer
//			// directly in the claim
//		}
//
//		// Get the policy amount (assume policy has a premiumAmount or totalAmount
//		// field)
//		// double policyAmount = policy.getPremiumAmount();
//		double policyAmount = policy.getTotalAmountPaid();
//		// Calculate the claim amount and apply 20% deduction for cancellation
//		double claimAmount;
//		if (policy.getMaturityDate().isAfter(LocalDate.now())) {
//			// Apply 20% deduction if canceled before maturity
//			double deductionPercentage = Double.parseDouble(keyValueRepository.getValueByKey("deduction_percentage"));
//			double deductionAmount = policyAmount * (deductionPercentage / 100);
//			claimAmount = policyAmount - deductionAmount;
//
//			// Mark as canceled and set the deduction details
//			claim.setCancel(true);
//			System.out.println("Policy canceled before maturity, applying 20% deduction.");
//		} else {
//			// No deduction if claimed after maturity
//			claimAmount = policyAmount;
//			claim.setCancel(false); // No cancellation flag after maturity
//			System.out.println("Policy claimed after maturity, no deduction applied.");
//		}
//
//		// Set claim details from the request
//		claim.setClaimAmount(claimAmount);
//		claim.setBankName(claimRequestDto.getBankName());
//		claim.setBranchName(claimRequestDto.getBranchName());
//		claim.setBankAccountId(claimRequestDto.getBankAccountId());
//		claim.setIfscCode(claimRequestDto.getIfscCode());
//		claim.setClaimedStatus("PENDING");
//
//		// Save the claim in the repository
//		claimRepository.save(claim);
//		System.out.println("Claim has been saved: " + claim);
//
//		return "Policy cancellation or claim has been created for the customer.";
//	}
//
//	@Override
//	public String buyPolicy(InsurancePolicyDto accountRequestDto, Customer customerId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String buyPolicy(InsurancePolicyDto accountRequestDto, Long customerId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void changePassword(Long customerId, ChangePasswordDto changePasswordDto) {
//		Customer customer = customerRepository.findById(customerId)
//				.orElseThrow(() -> new RuntimeException("Customer not found"));
//		if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), customer.getUser().getPassword())) {
//			throw new RuntimeException("Incorrect old password");
//		}
//		customer.getUser().setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
//		customerRepository.save(customer);
//
//	}
//
//	@Override
//	public CustomerResponseDto findCustomerByid(long customerId) {
//		Customer customer = customerRepository.findById(customerId)
//				.orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
//		return convertCustomerToCustomerResponseDto(customer);
//	}
//
//	private CustomerResponseDto convertCustomerToCustomerResponseDto(Customer customer) {
//		CustomerResponseDto customerDto = new CustomerResponseDto();
//
//		customerDto.setFirstName(customer.getFirstName());
//
//		customerDto.setLastName(customer.getLastName());
//
//		customerDto.setActive(customer.isActive());
//
//		customerDto.setDob(customer.getDob());
//		customerDto.setPhoneNumber(customer.getPhoneNumber());
//
//		return customerDto;
//	}
//
////	@Override
////	@Transactional
////	public void editCustomerDetails(Long customerId, CustomerRequestDto customerRequestDto) {
////		Customer customer = customerRepository.findById(customerId)
////				.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
////		// Update customer fields
////		if (customerRequestDto.getFirstName() != null)
////			customer.setFirstName(customerRequestDto.getFirstName());
////		if (customerRequestDto.getLastName() != null)
////			customer.setLastName(customerRequestDto.getLastName());
////		if (customerRequestDto.getIsActive() != null)
////			customer.setActive(customerRequestDto.getIsActive());
////
////		customer.setDob(customerRequestDto.getDob());
////		// if(customerRequestDto.getPhoneNumber()!=null)
////		customer.setPhoneNumber(customerRequestDto.getPhoneNumber());
////
////		// Set other fields as necessary
////		customerRepository.save(customer);
////
////	}
//
////	@Override
////	public InsurancePolicyDto getPolicyByid(Long customerId, Long policyId) {
////		// Fetch the InsurancePolicy entity by customerId and policyId
////		InsurancePolicy insurancePolicy = insurancePolicyRepository.findByCustomers_IdAndPolicyId(customerId, policyId);
////
////		if (insurancePolicy == null) {
////			throw new AllExceptions.PolicyNotFoundException(
////					"Policy not found for customer ID: " + customerId + " and policy ID: " + policyId);
////		}
////
////		// Convert the InsurancePolicy entity to InsurancePolicyDto
////		InsurancePolicyDto dto = new InsurancePolicyDto();
////		dto.setInsuranceId(insurancePolicy.getInsuranceId());
////		// dto.setInsuranceScheme(scheme != null ? scheme.getInsuranceScheme() : null);
////		// // Set scheme name
////		// dto.setInsuranceId(insurancePolicy.getInsuranceScheme().getInsuranceScheme())
////		// ;
////		// dto.setInsuranceScheme(insurancePolicy.getInsuranceId().getInsuranceScheme());
////		dto.setInsuranceSchemeId(insurancePolicy.getInsuranceScheme().getInsuranceSchemeId());
////		dto.setAgentId(insurancePolicy.getAgent() != null ? insurancePolicy.getAgent().getAgentId() : null);
////		dto.setClaimId(insurancePolicy.getClaim() != null ? insurancePolicy.getClaim().getClaimId() : null);
////		dto.setNominees(
////				insurancePolicy.getNominees().stream().map(n -> new NomineeDto(n)).collect(Collectors.toList()));
////		// dto.setNomineeIds(insurancePolicy.getNominees().stream().map(Nominee::getNomineeId).collect(Collectors.toList()));
////		// dto.setPaymentIds(insurancePolicy.getPayments().stream().map(Payment::getPaymentId).collect(Collectors.toList()));
////		// dto.setDocumentIds(insurancePolicy.getDocuments().stream().map(Document::getDocumentId).collect(Collectors.toSet()));
////		dto.setCustomerIds(
////				insurancePolicy.getCustomers().stream().map(Customer::getCustomerId).collect(Collectors.toList()));
////		dto.setIssuedDate(insurancePolicy.getIssuedDate());
////		dto.setMaturityDate(insurancePolicy.getMaturityDate());
////		dto.setPremiumAmount(insurancePolicy.getPremiumAmount());
////		dto.setPolicyStatus(insurancePolicy.getPolicyStatus());
////		dto.setActive(insurancePolicy.isActive());
////		dto.setPolicyTerm(insurancePolicy.getPolicyTerm());
////		dto.setInstallmentPeriod(insurancePolicy.getInstallmentPeriod());
////		// dto.setDocuments(insurancePolicy.getDocuments().stream().map(d -> new
////		// SubmittedDocumentDto(d)).collect(Collectors.toList()));
////
////		return dto;
////	}
//
//	@Override
//	public List<InsurancePolicyDto> getAllPoliciesByCustomerId(Long customerId) {
//		List<InsurancePolicy> policies = insurancePolicyRepository.findByCustomersCustomerId(customerId);
//
//		return policies.stream().map(policy -> {
//			InsuranceScheme scheme = policy.getInsuranceScheme();
//			InsurancePolicyDto dto = new InsurancePolicyDto();
//			dto.setInsuranceId(policy.getInsuranceId());
//			dto.setInsuranceSchemeId(scheme != null ? scheme.getInsuranceSchemeId() : null);
//			dto.setInsuranceScheme(scheme != null ? scheme.getInsuranceScheme() : null); // Set scheme name
//			dto.setAgentId(policy.getAgent() != null ? policy.getAgent().getAgentId() : null);
//			dto.setClaimId(policy.getClaim() != null ? policy.getClaim().getClaimId() : null);
//			dto.setNominees(policy.getNominees().stream().map(n -> new NomineeDto(n)).collect(Collectors.toList()));
//			// dto.setNomineeIds(policy.getNominees().stream().map(Nominee::getNomineeId).collect(Collectors.toList()));
//			// dto.setPaymentIds(policy.getPayments().stream().map(Payment::getPaymentId).collect(Collectors.toList()));
//			// dto.setDocumentIds(policy.getDocuments().stream().map(SubmittedDocument::getDocumentId).collect(Collectors.toSet()));
//			dto.setCustomerIds(
//					policy.getCustomers().stream().map(Customer::getCustomerId).collect(Collectors.toList()));
//			dto.setIssuedDate(policy.getIssuedDate());
//			dto.setMaturityDate(policy.getMaturityDate());
//			dto.setPremiumAmount(policy.getPremiumAmount());
//			dto.setPolicyStatus(policy.getPolicyStatus());
//			dto.setActive(policy.isActive());
//			dto.setPolicyTerm(policy.getPolicyTerm());
//			dto.setInstallmentPeriod(policy.getInstallmentPeriod());
//			// dto.setDocuments(policy.getDocuments().stream().map(d -> new
//			// SubmittedDocumentDto(d)).collect(Collectors.toList()));
//
//			return dto;
//		}).collect(Collectors.toList());
//	}
//
//	@Override
//	public List<Customer> getAllCustomers() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//

//
//	@Override
//	public void editCustomerDetails(Long customerId, CustomerRequestDto customerRequestDto) {
//		// TODO Auto-generated method stub
//		
//	}
//}
