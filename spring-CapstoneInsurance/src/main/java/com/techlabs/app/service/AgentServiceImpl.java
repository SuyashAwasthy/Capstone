package com.techlabs.app.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.ChangePasswordDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.ClaimResponseDto;
import com.techlabs.app.dto.UserResponseDto;
import com.techlabs.app.entity.Agent;
import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.ClaimStatus;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.entity.Policy;
import com.techlabs.app.entity.Role;
import com.techlabs.app.entity.Transaction;
import com.techlabs.app.entity.User;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.exception.AgentNotFoundException;
import com.techlabs.app.exception.ResourceNotFoundException;
import com.techlabs.app.repository.AgentRepository;
import com.techlabs.app.repository.CityRepository;
import com.techlabs.app.repository.ClaimRepository;
import com.techlabs.app.repository.CommissionRepository;
import com.techlabs.app.repository.CustomerRepository;
import com.techlabs.app.repository.InsurancePolicyRepository;
import com.techlabs.app.repository.PolicyRepository;
import com.techlabs.app.repository.RoleRepository;
import com.techlabs.app.repository.TransactionRepository;
import com.techlabs.app.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AgentServiceImpl implements AgentService {

	@Autowired
	private AgentRepository agentRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;

	@Autowired
	private ClaimRepository claimRepository;

	@Autowired
	private PolicyRepository policyRepository;

	@Autowired
	private CommissionRepository commissionRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	@Transactional
	public String registerAgent(AgentRequestDto agentRequestDto) {
		if (userRepository.existsByUsername(agentRequestDto.getUsername())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Username already exists!");
		}
		if (userRepository.existsByEmail(agentRequestDto.getEmail())) {
			throw new APIException(HttpStatus.BAD_REQUEST, "Email already exists!");
		}

		User user = new User();
		user.setUsername(agentRequestDto.getUsername());
		user.setEmail(agentRequestDto.getEmail());
		user.setPassword(passwordEncoder.encode(agentRequestDto.getPassword()));

		Role agentRole = roleRepository.findByName("ROLE_AGENT")
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Role not found: ROLE_AGENT"));
		Set<Role> roles = new HashSet<>();
		roles.add(agentRole);
		user.setRoles(roles);

		User savedUser = userRepository.save(user);

		City city = cityRepository.findById(agentRequestDto.getCity_id())
				.orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST,
						"City not found with id: " + agentRequestDto.getCity_id()));

		Agent agent = new Agent();
		agent.setAgentId(savedUser.getId());
		agent.setUser(savedUser);
		agent.setFirstName(agentRequestDto.getFirstName());
		agent.setLastName(agentRequestDto.getLastName());
		agent.setPhoneNumber(agentRequestDto.getPhoneNumber());
		agent.setCity(city);
		agent.setActive(true);
		agent.setVerified(false);
		agentRepository.save(agent);
		return "Agent Registered successfully";
	}

	@Override
	public AgentResponseDto getAgentById(Long id) {
		Agent agent = agentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Agent not found with id: " + id));

		return convertAgentToAgentResponseDto(agent);
	}

	private AgentResponseDto convertAgentToAgentResponseDto(Agent agent) {
		if (agent == null) {
			throw new IllegalArgumentException("Agent must not be null");
		}
		AgentResponseDto agentDto = new AgentResponseDto();

		agentDto.setAgentId(agent.getAgentId());
		agentDto.setName(agent.getFirstName() + " " + agent.getLastName()); // Combined name
		agentDto.setPhoneNumber(agent.getPhoneNumber());
		agentDto.setActive(agent.isActive());

		// Convert and set the associated City entity to CityResponseDto
//	    if (agent.getCity() != null) {
//	        CityResponseDto cityDto = convertCityToCityResponseDto(agent.getCity());
//	        agentDto.setCity(cityDto);
//	    }
		//
//	    // Convert and set the associated User entity to UserResponseDto
		if (agent.getUser() != null) {
			UserResponseDto userDto = new UserResponseDto();
			userDto.setId(agent.getUser().getId());
			userDto.setUsername(agent.getUser().getUsername());
			userDto.setEmail(agent.getUser().getEmail());
			// agentDto.setUserResponseDto(userDto);
		}

		// Convert and set associated lists (e.g., customers and commissions)
//	    if (agent.getCustomers() != null) {
//	        List<Customer> customers = agent.getCustomers().stream().collect(Collectors.toList());
//	        agentDto.setCustomers(customers);
//	    }

		if (agent.getCommissions() != null) {
			List<Commission> commissions = agent.getCommissions().stream().collect(Collectors.toList());
			agentDto.setCommissions(commissions);
		}

		return agentDto;
	}

	@Override
	public AgentResponseDto updateAgentProfile(Long id, AgentRequestDto agentRequestDto) {
		Agent agent = agentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + id));

		agent.setFirstName(agentRequestDto.getFirstName());
		agent.setLastName(agentRequestDto.getLastName());
		agent.setPhoneNumber(agentRequestDto.getPhoneNumber());

		Agent updatedAgent = agentRepository.save(agent);

		return new AgentResponseDto(updatedAgent.getAgentId(), updatedAgent.getFirstName(), updatedAgent.getLastName(),
				updatedAgent.getPhoneNumber());
	}

	@Override
	public InsurancePolicy registerPolicy(Long agentId, Map<String, Object> policyDetails) {
		// Find the agent by ID
		Agent agent = agentRepository.findById(agentId).orElseThrow(() -> new RuntimeException("Agent not found"));

		// Create a new InsurancePolicy from the details in the Map
		InsurancePolicy insurancePolicy = new InsurancePolicy();
//		insurancePolicy.setPolicyName((String) policyDetails.get("policyName"));
//		insurancePolicy.setPolicyType((String) policyDetails.get("policyType"));
		insurancePolicy.setPremiumAmount(Double.valueOf(policyDetails.get("premiumAmount").toString()));

		// Set the agent for the policy
		insurancePolicy.setAgent(agent);

		// Save the policy to the database
		return insurancePolicyRepository.save(insurancePolicy);
	}

//	@Override
//	public double calculateCommission(Long agentId, Long policyId) {
//	    // Find the agent by ID
//	    Agent agent = agentRepository.findById(agentId)
//	            .orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + agentId));
//
//	    // Find the insurance policy by ID
//	    InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(policyId)
//	            .orElseThrow(() -> new ResourceNotFoundException("Insurance Policy not found with ID: " + policyId));
//
//	    // Get the commission rate from the insurance policy
//	    double commissionRate = insurancePolicy.getRegisteredCommission(); // Assuming InsurancePolicy has a commission rate
//	    double policyPremium = insurancePolicy.getPremiumAmount(); // Assuming InsurancePolicy has a premium amount
//
//	    // Calculate commission
//	    double commission = policyPremium * (commissionRate / 100);
//
//	    // Create the Commission entry and set all required fields
//	    Commission commissionEntry = new Commission();
//	    commissionEntry.setAgent(agent);
//	    commissionEntry.setInsurancePolicy(insurancePolicy);
//	    commissionEntry.setAmount(commission);
//	    
//	    // Set the commission type (ensure to provide the correct value based on your business logic)
//	    commissionEntry.setCommissionType("Standard"); // Example value, modify based on your use case
//	    
//	    // Set the date of commission
//	    commissionEntry.setDate(LocalDateTime.now());
//
//	    // Save the commission
//	    commissionRepository.save(commissionEntry);
//
//	    return commission;
//	}

	@Override
	public double calculateCommission(Long agentId, Long policyId) {
		// Retrieve the agent by ID
		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + agentId));

		// Retrieve the insurance policy by ID
		InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy not found with ID: " + policyId));

		InsuranceScheme insuranceScheme = insurancePolicy.getInsuranceScheme();
		// Retrieve the commission rate and policy premium
		double commissionRate = insuranceScheme.getProfitRatio(); // Commission rate should be > 0
		double policyPremium = insurancePolicy.getPremiumAmount(); // Policy premium should be > 0

		// Check if the values are correct (for debugging)
		System.out.println("Commission Rate: " + commissionRate);
		System.out.println("Policy Premium: " + policyPremium);

		// If commission rate or policy premium are 0, log an error
		if (commissionRate <= 0) {
			throw new IllegalArgumentException("Invalid commission rate: " + commissionRate);
		}
		if (policyPremium <= 0) {
			throw new IllegalArgumentException("Invalid policy premium: " + policyPremium);
		}

		// Calculate the commission
		double commission = policyPremium * (commissionRate / 100);

		// Log the calculated commission (for debugging)
		System.out.println("Calculated Commission: " + commission);

		// Save commission data in the Commission table
		Commission commissionEntry = new Commission();
		commissionEntry.setAgent(agent);
		commissionEntry.setInsurancePolicy(insurancePolicy);
		commissionEntry.setAmount(commission);

		// Set commission type and date
		commissionEntry.setCommissionType("Standard"); // Modify commission type based on business logic
		commissionEntry.setDate(LocalDateTime.now());

		// Save the commission in the repository
		commissionRepository.save(commissionEntry);

		return commission;
	}

	@Override
	@Transactional
	public void withdrawCommission(Long agentId, double amount, Long insurancePolicyId) {
		// Fetch the agent
		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + agentId));

		// Fetch the insurance policy
		InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(insurancePolicyId).orElseThrow(
				() -> new ResourceNotFoundException("InsurancePolicy not found with ID: " + insurancePolicyId));

		// Fetch total commission amount for the agent
		Double totalCommission = commissionRepository.getTotalCommissionForAgent(agentId);

		// Check if total commission is null or less than the requested withdrawal
		// amount
		if (totalCommission == null || amount > totalCommission) {
			throw new IllegalArgumentException("Insufficient commission balance to withdraw");
		}

		// Create a transaction for commission withdrawal
		Transaction transaction = new Transaction();
		transaction.setAgent(agent);
		transaction.setAmount(amount);
		transaction.setTransactionType("WITHDRAWAL");
		transaction.setDate(LocalDateTime.now());
		transaction.setInsurancePolicy(insurancePolicy);

		transactionRepository.save(transaction);

		// Update agent's commission balance
		updateCommissionBalance(agentId, amount);
	}

	private void updateCommissionBalance(Long agentId, double amount) {
		// Fetch all commissions for the agent
		List<Commission> commissions = commissionRepository.findCommissionsForAgent(agentId);

		// Deduct the withdrawal amount from commissions
		double remainingAmount = amount;

		for (Commission commission : commissions) {
			if (remainingAmount <= 0)
				break;

			double currentAmount = commission.getAmount();
			if (currentAmount > remainingAmount) {
				commission.setAmount(currentAmount - remainingAmount);
				remainingAmount = 0;
			} else {
				remainingAmount -= currentAmount;
				commission.setAmount(0.0);
			}

			commissionRepository.save(commission);
		}

		// Ensure that remainingAmount is zero or less
		if (remainingAmount > 0) {
			throw new IllegalStateException("Error: Not enough commission balance to complete the withdrawal.");
		}
	}

	@Override
	public List<Double> getEarningsReport(Long agentId) {
		// Fetch the Agent entity by its ID
		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + agentId));

		// Fetch transactions for the given agent with the type "WITHDRAWAL"
		List<Transaction> earnings = transactionRepository.findAllByAgentAndTransactionType(agent, "WITHDRAWAL");

		// Map the list of Transaction objects to a list of amounts
		return earnings.stream().map(Transaction::getAmount) // Extract amount from each transaction
				.collect(Collectors.toList()); // Collect into a List<Double>
	}

	@Override
	public List<Double> getCommissionReport(Long agentId) {
		// Fetch the Agent entity by its ID
		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new ResourceNotFoundException("Agent not found with ID: " + agentId));

		// Fetch all commissions for the agent from the Commission table
		List<Commission> commissions = commissionRepository.findByAgent(agent);

		// Extract the amount from each commission and return as a list
		return commissions.stream().map(Commission::getAmount).collect(Collectors.toList());
	}

	@Override
	public String agentclaimPolicy(ClaimRequestDto claimRequestDto, Long agentId) {
		InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(claimRequestDto.getPolicyId())
				.orElseThrow(() -> new RuntimeException("Policy not found"));

		// Agent's Commission Claim
		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new AgentNotFoundException("Agent not found"));

		Claim agentClaim = new Claim();
		agentClaim.setClaimAmount(claimRequestDto.getClaimAmount());
		agentClaim.setBankName(claimRequestDto.getBankName());
		agentClaim.setBranchName(claimRequestDto.getBranchName());
		agentClaim.setBankAccountId(claimRequestDto.getBankAccountId());
		agentClaim.setIfscCode(claimRequestDto.getIfscCode());
		agentClaim.setClaimedStatus(ClaimStatus.PENDING.name());
		agentClaim.setPolicy(insurancePolicy);
		agentClaim.setAgent(agent); // Set the agent reference

		claimRepository.save(agentClaim);

		return "Claim of " + claimRequestDto.getClaimAmount() + " has been successfully created for policy ID "
				+ claimRequestDto.getPolicyId() + ". The claim is pending approval.";
	}

	@Override
	public void changePassword(Long id, ChangePasswordDto changePasswordDto) {
		Agent agent = agentRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
		if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), agent.getUser().getPassword())) {
			throw new RuntimeException("Incorrect old password");
		}
		agent.getUser().setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
		agentRepository.save(agent);

	}
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Override
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

}
