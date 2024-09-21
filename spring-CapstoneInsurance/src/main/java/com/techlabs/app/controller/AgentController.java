package com.techlabs.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlabs.app.dto.AgentRequestDto;
import com.techlabs.app.dto.AgentResponseDto;
import com.techlabs.app.dto.ChangePasswordDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.ContactFormRequestDto;
import com.techlabs.app.dto.ContactReplyRequestDto;
import com.techlabs.app.dto.CustomerRequestDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.entity.ContactMessage;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.Policy;
import com.techlabs.app.service.AgentService;
import com.techlabs.app.service.ContactFormService;
import com.techlabs.app.service.CustomerService;
import com.techlabs.app.service.EmployeeService;
import com.techlabs.app.service.InsurancePolicyService;
import com.techlabs.app.util.PagedResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/E-Insurance/agent")
@CrossOrigin(origins="http://localhost:3000")
@PreAuthorize("hasRole('AGENT')")
public class AgentController {
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private InsurancePolicyService policyService;

	// Register a new agent
	@PostMapping("/register")
	public ResponseEntity<String> registerAgent(@Valid @RequestBody AgentRequestDto agentRequestDto) {
		return new ResponseEntity<>(agentService.registerAgent(agentRequestDto), HttpStatus.CREATED);
	}

	// Get an agent by ID
	 @GetMapping("/agentById/{agentId}")
     public ResponseEntity<AgentResponseDto> viewAgentById(@PathVariable(name = "agentId") long agentId) {
         return new ResponseEntity<AgentResponseDto>(agentService.getAgentById(agentId), HttpStatus.OK);
     }
     

	// Update an agent's profile
	@PutMapping("/{id}")
	public ResponseEntity<AgentResponseDto> updateAgentProfile(@PathVariable Long id, @Valid @RequestBody AgentRequestDto agentRequestDto) {
		return new ResponseEntity<>(agentService.updateAgentProfile(id, agentRequestDto), HttpStatus.OK);
	}

	// Change an agent's password
	@PutMapping("/{id}/change-password")
	public ResponseEntity<String> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordDto changePasswordDto) {
		agentService.changePassword(id, changePasswordDto);
		return ResponseEntity.ok("Password changed successfully"); 
	}

	// Register a new policy under an agent
//	@PostMapping("/{agentId}/policies")
//	public InsurancePolicy registerPolicy(@PathVariable Long agentId, @RequestBody Policy policy) {
//		return agentService.registerPolicy(agentId, policy);
//	}

	// Calculate commission for a policy
	@GetMapping("/{agentId}/commissions")
	public double calculateCommission(@PathVariable Long agentId, @RequestParam Long policyId) {
		return agentService.calculateCommission(agentId, policyId);
	}

	// Get a list of all agents
//	@GetMapping
//	public List<AgentResponseDto> getAllAgents() {
//		return agentService.getAllAgents();
//	}

	// Withdraw commission for an agent
	@PostMapping("/withdraw")
    public ResponseEntity<String> withdrawCommission(@RequestParam Long agentId, 
                                                      @RequestParam double amount,
                                                      @RequestParam Long insurancePolicyId) {
        agentService.withdrawCommission(agentId, amount, insurancePolicyId);
        return ResponseEntity.ok("Withdrawal successful");
    }
//
//	// Get a list of policies under an agent
//	@GetMapping("/{agentId}/policies")
//	public List<Policy> getAgentPolicies(@PathVariable Long agentId) {
//		return agentService.getAgentPolicies(agentId);
//	}

	// Get an earnings report for an agent
	@GetMapping("/{agentId}/earnings")
	public List<Double> getEarningsReport(@PathVariable Long agentId) {
		return agentService.getEarningsReport(agentId);
	}

	// Get a commission report for an agent
	@GetMapping("/{agentId}/commissions/report")
	public List<Double> getCommissionReport(@PathVariable Long agentId) {
		return agentService.getCommissionReport(agentId);
	}

	@PostMapping("/claim")
	  public ResponseEntity<String> AgentclaimPolicy(@RequestBody ClaimRequestDto claimRequestDto,
	                                            @RequestParam Long agentId) {
	      String response = agentService.agentclaimPolicy(claimRequestDto, agentId);
	      return ResponseEntity.ok(response);
	  }
	
	@Autowired
	CustomerService customerService;
	
//	@GetMapping("/customers/{customerId}") 
//    public ResponseEntity<Void> editCustomerDetails(@PathVariable Long customerId) { 
//        customerService.getAllCustomers(); 
//        return ResponseEntity.noContent().build(); 
//    } 
	
	@GetMapping("/get-all-customers")
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = customerService.getAllCustomers();
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}
	
//	@GetMapping("/get-all-customers")
//	public ResponseEntity<Page<Customer>> getAllCustomers(
//	        @RequestParam(defaultValue = "0") int page,
//	        @RequestParam(defaultValue = "10") int size,
//	        @RequestParam(defaultValue = "id") String sortBy) {
//	    
//	    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
//	    Page<Customer> customers = customerService.getAllCustomers(pageable);
//	    return new ResponseEntity<>(customers, HttpStatus.OK);
//	}

	
	@Autowired
    private ContactFormService contactFormService;

    @GetMapping("/contact/messages/{agentId}")
    public ResponseEntity<List<ContactMessage>> getContactMessages(@PathVariable Long agentId) {
        List<ContactMessage> messages = contactFormService.getAllContactMessagesForAgent(agentId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping("/contact/reply")
    public ResponseEntity<String> replyToContactMessage(@RequestBody ContactReplyRequestDto replyRequest) {
        contactFormService.submitReply(replyRequest);
        return new ResponseEntity<>("Reply submitted successfully", HttpStatus.OK);
    }
    
    @GetMapping("/queries")
    public ResponseEntity<List<ContactFormRequestDto>> viewAllQueries() {
        List<ContactFormRequestDto> queries = contactFormService.getAllQueries();
        return new ResponseEntity<>(queries, HttpStatus.OK);
    }
    
//    @GetMapping("/queries")
//    public ResponseEntity<Page<ContactFormRequestDto>> viewAllQueries(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id") String sortBy) {
//        
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
//        Page<ContactFormRequestDto> queries = contactFormService.getAllQueries(pageable);
//        return new ResponseEntity<>(queries, HttpStatus.OK);
//    }

    
//    @GetMapping("/{agentId}/policies")
//    public ResponseEntity<Page<Policy>> getAgentPolicies(
//            @PathVariable Long agentId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id") String sortBy) {
//        
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
//        Page<Policy> policies = agentService.getAgentPolicies(agentId, pageable);
//        return new ResponseEntity<>(policies, HttpStatus.OK);
//    }
    
  //  @Secured("AGENT")
    @Operation(summary = "Get All Claints Of An Agent")
    @GetMapping("/all-clients")
    public ResponseEntity<PagedResponse<InsurancePolicyDto>> getAllPoliciesUnderAnAgent(
        @RequestParam(required = false) Long id, @RequestParam(required = false) Long customerId,
        @RequestParam(required = false) String name, @RequestParam(required = false) String policyStatus,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        HttpServletRequest request) {
    //  logger.info("Fetching All The Policies");
      PagedResponse<InsurancePolicyDto> policies = policyService.getAllPoliciesUnderAnAgent(id, customerId, name,
          policyStatus, page, size,request);

      return new ResponseEntity<>(policies, HttpStatus.OK);
    }


}
