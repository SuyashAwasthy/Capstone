package com.techlabs.app.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techlabs.app.dto.CancellationRequestDto;
import com.techlabs.app.dto.ClaimRequestDto;
import com.techlabs.app.dto.ContactFormRequestDto;
import com.techlabs.app.dto.InsurancePolicyDto;
import com.techlabs.app.dto.PaymentReceiptDto;
import com.techlabs.app.dto.PolicyAccountRequestDto;
import com.techlabs.app.dto.RegisterDto;
import com.techlabs.app.dto.StripeChargeDto;
import com.techlabs.app.entity.Claim;
import com.techlabs.app.entity.Customer;
import com.techlabs.app.entity.DocumentType;
import com.techlabs.app.entity.Documentt;
import com.techlabs.app.entity.InsurancePolicy;
import com.techlabs.app.entity.InsuranceScheme;
import com.techlabs.app.exception.APIException;
import com.techlabs.app.repository.InsuranceSchemeRepository;
import com.techlabs.app.service.ClaimService;
import com.techlabs.app.service.ContactFormService;
import com.techlabs.app.service.CustomerDocumentService;
import com.techlabs.app.service.CustomerService;
import com.techlabs.app.service.InsurancePolicyService;
import com.techlabs.app.service.InsuranceSchemeService;
import com.techlabs.app.service.PaymentReceiptService;
import com.techlabs.app.service.StripeService;
import org.springframework.ui.Model;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.HttpHeaders;



import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/E-Insurance/customer")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('CUSTOMER')")

public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private InsurancePolicyService insurancePolicyService;

	@Autowired
	private InsuranceSchemeService insuranceSchemeRepository;

	@Autowired
	private ClaimService claimService;
	
	@Autowired
	StripeService stripService;

	 

	public CustomerController(CustomerService customerService, InsurancePolicyService insurancePolicyService,
			InsuranceSchemeService insuranceSchemeRepository, ClaimService claimService, StripeService stripService) {
		super();
		this.customerService = customerService;
		this.insurancePolicyService = insurancePolicyService;
		this.insuranceSchemeRepository = insuranceSchemeRepository;
		this.claimService = claimService;
		this.stripService = stripService;
	}

	@PostMapping("/registerPolicyForCustomer")
	public ResponseEntity<String> registerPolicyForCustomer(@RequestParam long customerId, @RequestParam long policyId,
			@RequestParam long agentId) {
		try {
			String response = insurancePolicyService.registerPolicyForCustomer(customerId, policyId, agentId);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/add")
	public ResponseEntity<String> addCustomer(@RequestBody RegisterDto registerDto) {
		try {
			customerService.addCustomer(registerDto);
			return new ResponseEntity<>("Customer registered successfully!", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Error registering customer: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/get-all-customers")
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = customerService.getAllCustomers();
		return new ResponseEntity<>(customers, HttpStatus.OK);
	}
//	    
//	    @Operation(summary = "Create Insurance Policy")
//		@PostMapping("/createPolicy")
//		public ResponseEntity<String> createInsurancePolicy(@RequestBody InsurancePolicyDto insurancePolicyDto) {
//			logger.info("Creating insurance policy: {}", insurancePolicyDto);
//			String response = customerService.createInsurancePolicy(insurancePolicyDto);
//			return new ResponseEntity<>(response, HttpStatus.CREATED);
//		}

//	    @PostMapping("/customers/{customerId}/buy-policy")
//	    public ResponseEntity<String> buyPolicy(@RequestBody PolicyAccountRequestDto accountRequestDto,@PathVariable(name = "customerId") long customerId){
//	    	System.out.println("iuytre");
//	      return new ResponseEntity<String>(customerService.buyPolicy(accountRequestDto,customerId),HttpStatus.OK);
//	      
//	    }

//	@PostMapping("/{customerId}/buy-policy")
//	public ResponseEntity<String> buyPolicy(@RequestBody InsurancePolicyDto accountRequestDto,
//			@PathVariable(name = "customerId") long customerId) {
//		return new ResponseEntity<String>(customerService.buyPolicy(accountRequestDto, customerId), HttpStatus.OK);
//
//	}
	
	@PostMapping("/{customerId}/buy-policy")
	public ResponseEntity<InsurancePolicyDto> buyPolicy(@RequestBody InsurancePolicyDto accountRequestDto,
			@PathVariable(name = "customerId") long customerId) {
		System.out.println("starting buy policy");
		
		return new ResponseEntity<>(customerService.buyPolicy(accountRequestDto, customerId), HttpStatus.OK);

	}
	
	

	@PostMapping("/buyWithoutAgent")
	public ResponseEntity<String> buyPolicyWithoutAgent(@RequestBody @Valid InsurancePolicyDto accountRequestDto,
			@RequestParam long customerId) {
		InsuranceScheme insuranceScheme = insurancePolicyService
				.getInsuranceScheme(accountRequestDto.getInsuranceSchemeId());

		String response = customerService.buyPolicyWithoutAgent(accountRequestDto, customerId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/claim")
	public ResponseEntity<String> claimPolicy(@RequestBody ClaimRequestDto claimRequestDto,@RequestParam Long customerId){
		String response =customerService.claimPolicy(claimRequestDto,customerId);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/cancel")
	 public ResponseEntity<String> cancelPolicy(@RequestBody CancellationRequestDto cancellationRequest, @RequestParam Long customerId) {
        String response = customerService.requestPolicyCancellation(cancellationRequest, customerId);
        return ResponseEntity.ok(response);
    }
	@PostMapping("/test")
	public String testing(@RequestBody StripeChargeDto stripeCharge) {
		stripService.charge(stripeCharge);
		return "success";

	}
	
	@PostMapping("/cancelPolicy")
    public ResponseEntity<String> customerCancelPolicy(@RequestBody ClaimRequestDto claimRequestDto,
                                                       @RequestParam Long customerId) {
        String response = customerService.customerCancelPolicy(claimRequestDto, customerId);
        return ResponseEntity.ok(response);
    }
//	 @PostMapping("/uploadDocument/{customerId}")
//	    public String uploadDocument(@PathVariable("customerId") Long customerId,
//	                                 @RequestParam("documentType") DocumentType documentType,
//	                                 @RequestParam("document") MultipartFile file) {
//	        customerDocumentService.uploadDocument(customerId, documentType, file);
//	        return "redirect:/customer/documents/" + customerId;  // Redirect to view the uploaded documents
//	    }
//	
//	 @GetMapping("/documents/{customerId}")
//	    public String viewDocuments(@PathVariable("customerId") Long customerId, Model model) {
//	        List<Documentt> documents = customerDocumentService.getCustomerDocuments(customerId);
//	        model.addAttribute("documents", documents);
//	        return "document-list";  // JSP/HTML page that lists documents
//	    }
//
//	    @GetMapping("/verify/{documentId}/{employeeId}")
//	    public String verifyDocument(@PathVariable("documentId") long documentId, @PathVariable("employeeId") Long employeeId) {
//	        customerDocumentService.verifyDocument(documentId, employeeId);
//	        return "redirect:/customer/documents";
//	    }
//
//	    @GetMapping("/download/{documentId}")
//	    public ResponseEntity<Resource> downloadDocument(@PathVariable("documentId") int documentId) {
//	        Documentt document = customerDocumentService.downloadDocument(documentId);
//	        try {
//	            Path file = Paths.get(document.getContent().toString());
//	            Resource resource = new UrlResource(file.toUri());
//
//	            return ResponseEntity.ok()
//	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getDocumentName() + "\"")
//	                    .body(resource);
//
//	        } catch (Exception e) {
//	            throw new APIException(HttpStatus.INTERNAL_SERVER_ERROR, "Error downloading document.");
//	        }
//	    }

//	    @GetMapping("/delete/{documentId}")
//	    public String deleteDocument(@PathVariable("documentId") int documentId) {
//	        customerDocumentService.deleteDocument(documentId);
//	        return "redirect:/customer/documents";
//	    }
	 
//	    @GetMapping("/customer/{customerId}")
//	    public ResponseEntity<List<InsurancePolicy>> getPoliciesByCustomer(
//	            @PathVariable Long customerId) {
//
//	        List<InsurancePolicy> policies = insurancePolicyService.getPoliciesByCustomerId(customerId);
//	        return ResponseEntity.ok(policies);
//	    }
//	   
	
	@Autowired
    private PaymentReceiptService paymentReceiptService;
	
	// Payment Receipt Module
    @PostMapping("/generate-receipt")
    public ResponseEntity<PaymentReceiptDto> generatePaymentReceipt(@RequestBody PaymentReceiptDto receiptRequest) {
        PaymentReceiptDto receiptDto = paymentReceiptService.generateReceipt(receiptRequest);
        return new ResponseEntity<>(receiptDto, HttpStatus.OK);
    }
//
//    @GetMapping("/receipt/{receiptId}")
//    public ResponseEntity<PaymentReceiptDto> getPaymentReceipt(@PathVariable Long receiptId) {
//        PaymentReceiptDto receiptDto = paymentReceiptService.getReceiptById(receiptId);
//        return new ResponseEntity<>(receiptDto, HttpStatus.OK);
//    }
//
    // Contact Form
//    @PostMapping("/contact")
//    public ResponseEntity<String> submitContactForm(@RequestBody ContactFormRequestDto contactFormRequest) {
//        contactFormService.submitForm(contactFormRequest);
//        return new ResponseEntity<>("Form submitted successfully", HttpStatus.OK);
//    }
	
	@Autowired
    private ContactFormService contactFormService;

    @PostMapping("/contact")
    public ResponseEntity<String> submitContactForm(@RequestBody ContactFormRequestDto contactFormRequest) {
        contactFormService.submitForm(contactFormRequest);
        return new ResponseEntity<>("Form submitted successfully", HttpStatus.OK);
    }

    // View Customer Query (Admin/Employee can see)
    @GetMapping("/queries")
    public ResponseEntity<List<ContactFormRequestDto>> viewAllQueries() {
        List<ContactFormRequestDto> queries = contactFormService.getAllQueries();
        return new ResponseEntity<>(queries, HttpStatus.OK);
    }
}

//package com.techlabs.app.controller;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
//import com.techlabs.app.dto.CancellationRequestDto;
//import com.techlabs.app.dto.ChangePasswordDto;
//import com.techlabs.app.dto.ClaimRequestDto;
//import com.techlabs.app.dto.CustomerRequestDto;
//import com.techlabs.app.dto.CustomerResponseDto;
//import com.techlabs.app.dto.InsurancePolicyDto;
//import com.techlabs.app.dto.PaymentRequest;
//import com.techlabs.app.dto.PolicyAccountRequestDto;
//import com.techlabs.app.dto.RegisterDto;
//import com.techlabs.app.dto.StripeChargeDto;
//import com.techlabs.app.entity.Agent;
//import com.techlabs.app.entity.Claim;
//import com.techlabs.app.entity.Customer;
//import com.techlabs.app.entity.DocumentStatus;
//import com.techlabs.app.entity.DocumentType;
//import com.techlabs.app.entity.Documentt;
//import com.techlabs.app.entity.InsurancePolicy;
//import com.techlabs.app.entity.InsuranceScheme;
//import com.techlabs.app.entity.PayementStatus;
//import com.techlabs.app.entity.Payment;
//import com.techlabs.app.entity.PaymentType;
//import com.techlabs.app.entity.PolicyStatus;
//import com.techlabs.app.entity.SubmittedDocument;
//import com.techlabs.app.exception.APIException;
//import com.techlabs.app.exception.AllExceptions;
//import com.techlabs.app.repository.InsurancePolicyRepository;
//import com.techlabs.app.repository.InsuranceSchemeRepository;
//import com.techlabs.app.repository.PaymentRepository;
//import com.techlabs.app.repository.SubmittedDocumentRepository;
//import com.techlabs.app.service.ClaimService;
//import com.techlabs.app.service.CustomerDocumentService;
//import com.techlabs.app.service.CustomerService;
//import com.techlabs.app.service.InsurancePolicyService;
//import com.techlabs.app.service.InsuranceSchemeService;
//import com.techlabs.app.service.StripeService;
//import com.techlabs.app.util.PagedResponse;
//
//import org.springframework.ui.Model;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import org.springframework.http.HttpHeaders;
//
//import io.swagger.v3.oas.annotations.Operation;
//import jakarta.validation.Valid;
//
//@RestController
//@RequestMapping("/E-Insurance/customer")
//@PreAuthorize("hasRole('CUSTOMER')")
//@CrossOrigin(origins = "http://localhost:3000")
//
//public class CustomerController {
//
//	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
//
//	@Autowired
//	private CustomerService customerService;
//
//	@Autowired
//	private InsurancePolicyService insurancePolicyService;
//
//	@Autowired
//	private InsuranceSchemeService insuranceSchemeRepository;
//
//	@Autowired
//	private ClaimService claimService;
//
//	@Autowired
//	StripeService stripService;
//
//	@Autowired
//	private InsurancePolicyRepository insurancePolicyRepository;
//
//	@Autowired
//	private PaymentRepository paymentRepository;
//
//	@Autowired
//	private SubmittedDocumentRepository submittedDocumentRepository;
//
//	public CustomerController() {
//		Stripe.apiKey = "sk_test_51Pz1w4LAuyjp8hN9EdVGJqAZKwDzk4G5YaWkCoGBAmtk80iPa4yFu6PEm3wtoNHpV2JIex6PCUbRAVx5drqoqecv00HIFucvei";
//	}
//
//	public CustomerController(CustomerService customerService, InsurancePolicyService insurancePolicyService,
//			InsuranceSchemeService insuranceSchemeRepository, ClaimService claimService, StripeService stripService) {
//		super();
//		this.customerService = customerService;
//		this.insurancePolicyService = insurancePolicyService;
//		this.insuranceSchemeRepository = insuranceSchemeRepository;
//		this.claimService = claimService;
//		this.stripService = stripService;
//	}
//
//	@PostMapping("/registerPolicyForCustomer")
//	public ResponseEntity<String> registerPolicyForCustomer(@RequestParam long customerId, @RequestParam long policyId,
//			@RequestParam long agentId) {
//		try {
//			String response = insurancePolicyService.registerPolicyForCustomer(customerId, policyId, agentId);
//			return new ResponseEntity<>(response, HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//		}
//
//	}
//
//	@PostMapping("/add")
//	public ResponseEntity<String> addCustomer(@RequestBody RegisterDto registerDto) {
//		try {
//			customerService.addCustomer(registerDto);
//			return new ResponseEntity<>("Customer registered successfully!", HttpStatus.CREATED);
//		} catch (Exception e) {
//			return new ResponseEntity<>("Error registering customer: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//		}
//	}
//
//	@Operation(summary = "view Customer By ID")
//	@GetMapping("/view-customer-by-id/{customerId}")
//	public ResponseEntity<CustomerResponseDto> viewCustomerbyId(@PathVariable(name = "customerId") long customerId) {
//		return new ResponseEntity<CustomerResponseDto>(customerService.findCustomerByid(customerId), HttpStatus.OK);
//	}
//
//	@PutMapping("/customers/{customerId}")
//	public ResponseEntity<Void> editCustomerDetails(@PathVariable Long customerId,
//			@RequestBody CustomerRequestDto customerRequestDto) {
//		customerService.editCustomerDetails(customerId, customerRequestDto);
//		return ResponseEntity.noContent().build();
//	}
//
//	@Operation(summary = "TO get all Customers")
//	@GetMapping("/get-all-customers")
//	public ResponseEntity<PagedResponse<CustomerResponseDto>> getAllCustomers(
//			@RequestParam(value = "page", defaultValue = "0") int page,
//			@RequestParam(value = "size", defaultValue = "10") int size) {
//
//		logger.info("Received request to get all customers with page {} and size {}.", page, size);
//		PagedResponse<CustomerResponseDto> pagedResponse = customerService.getAllCustomers(page, size);
//		logger.info("Retrieved {} customers on page {}.", pagedResponse.getContent().size(), page);
//		return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
//	}
//
//	@PostMapping("/{customerId}/buy-policy")
//	public ResponseEntity<String> buyPolicy(@RequestBody InsurancePolicyDto accountRequestDto,
//			@PathVariable(name = "customerId") long customerId) {
//		System.out.println("starting buy policy");
//
//		return new ResponseEntity<String>(customerService.buyPolicy(accountRequestDto, customerId), HttpStatus.OK);
//
//	}
//
//	@PostMapping("/{customerId}/buyWithoutAgent")
//	public ResponseEntity<String> buyPolicyWithoutAgent(@RequestBody @Valid InsurancePolicyDto accountRequestDto,
//			@PathVariable(name = "customerId") long customerId) {
//		System.out.println(accountRequestDto.getNominees());
//		InsuranceScheme insuranceScheme = insurancePolicyService
//				.getInsuranceScheme(accountRequestDto.getInsuranceSchemeId());
//
//		String response = customerService.buyPolicyWithoutAgent(accountRequestDto, customerId);
//		return ResponseEntity.ok(response);
//	}
//
//	@PostMapping("/claim")
//	public ResponseEntity<String> claimPolicy(@RequestBody ClaimRequestDto claimRequestDto,
//			@RequestParam Long customerId) {
//		String response = customerService.claimPolicy(claimRequestDto, customerId);
//		return ResponseEntity.ok(response);
//	}
//
//	@PostMapping("/cancel")
//	public ResponseEntity<String> cancelPolicy(@RequestBody CancellationRequestDto cancellationRequest,
//			@RequestParam Long customerId) {
//		String response = customerService.requestPolicyCancellation(cancellationRequest, customerId);
//		return ResponseEntity.ok(response);
//	}
//
//	@PostMapping("/test")
//	public String testing(@RequestBody StripeChargeDto stripeCharge) {
//		stripService.charge(stripeCharge);
//		return "success";
//	}
//
//	@PostMapping("/cancelPolicy")
//	public ResponseEntity<String> customerCancelPolicy(@RequestBody ClaimRequestDto claimRequestDto,
//			@RequestParam Long customerId) {
//		String response = customerService.customerCancelPolicy(claimRequestDto, customerId);
//		return ResponseEntity.ok(response);
//	}
//
//	@PutMapping("/{customerId}/change-password")
//	public ResponseEntity<String> changePassword(@PathVariable Long customerId,
//			@RequestBody ChangePasswordDto changePasswordDto) {
//		customerService.changePassword(customerId, changePasswordDto);
//		return ResponseEntity.ok("Password changed successfully");
//	}
//
////	@PostMapping("/create-payment-intent")
////	public ResponseEntity<Map<String, Object>> createPaymentIntent(@RequestBody PaymentRequest request) {
////		Map<String, Object> response = new HashMap<>();
////		Payment payment = new Payment();
////
////		try {
////			// Fetch the insurance policy
////			InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(request.getPolicyId())
////					.orElseThrow(() -> new AllExceptions.PlanNotFoundException(
////							"Policy with ID : " + request.getPolicyId() + " cannot be found"));
////
////			// Create PaymentIntent parameters
////			Map<String, Object> params = new HashMap<>();
////			params.put("amount", (int) (request.getAmount() * 100)); // Convert to the smallest currency unit
////			params.put("currency", "usd"); // Adjust the currency as needed
////			params.put("payment_method", request.getPaymentMethodId());
////
////			// Set payment details
////			payment.setAmount(request.getAmount());
////			payment.setTax(request.getTax());
////			payment.setTotalPayment(request.getTotalPayment());
////			payment.setPaymentType(request.getPaymentType().equalsIgnoreCase("debit") ? PaymentType.DEBIT_CARD.name()
////					: PaymentType.CREDIT_CARD.name());
////			payment.setPaymentDate(LocalDateTime.now());
////			payment.setInsurancePolicy(insurancePolicy);
////
////			// Calculate the total paid amount and update policy status
////			double totalPaidAmount = request.getAmount() + insurancePolicy.getTotalAmountPaid();
////			if (totalPaidAmount > insurancePolicy.getPremiumAmount()) {
////				throw new AllExceptions.PlanNotFoundException(
////						"Your payment is complete. You can now claim your policy.");
////			}
////
////			if (totalPaidAmount >= insurancePolicy.getPremiumAmount()) {
////				insurancePolicy.setPolicyStatus(PolicyStatus.COMPLETE.name());
////			} else if (PolicyStatus.PENDING.name().equals(insurancePolicy.getPolicyStatus())) {
////				insurancePolicy.setPolicyStatus(PolicyStatus.ACTIVE.name());
////				updateSubmittedDocumentsStatus(insurancePolicy);
////			}
////
////			if (!insurancePolicy.getVerified()) {
////				insurancePolicy.setVerified(true);
////			}
////
////			payment.setPaymentStatus(PayementStatus.PAID.name());
////			insurancePolicy.setTotalAmountPaid(totalPaidAmount);
////
////			// Handle agent commission if applicable
////			Agent agent = insurancePolicy.getAgent();
////			if (agent != null && agent.isActive() && agent.isVerified()) {
////				handleAgentCommission(agent, insurancePolicy, request.getAmount());
////			}
////
////			// Save payment and update policy
////			paymentRepository.save(payment);
////			List<Payment> payments = insurancePolicy.getPayments();
////			payments.add(payment);
////			insurancePolicy.setPayments(payments);
////			insurancePolicyRepository.save(insurancePolicy);
////
////			// Create PaymentIntent with Stripe
////			Stripe.apiKey = "sk_test_51Pz1w4LAuyjp8hN9EdVGJqAZKwDzk4G5YaWkCoGBAmtk80iPa4yFu6PEm3wtoNHpV2JIex6PCUbRAVx5drqoqecv00HIFucvei";
////			PaymentIntent paymentIntent = PaymentIntent.create(params);
////
////			// Return client secret to the client
////			response.put("clientSecret", paymentIntent.getClientSecret());
////			return ResponseEntity.ok(response);
////
////		} catch (StripeException e) {
////			// Handle Stripe API exceptions
////			payment.setPaymentStatus(PayementStatus.UNPAID.name());
////			paymentRepository.save(payment);
////			response.put("error", e.getMessage());
////			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
////		} catch (Exception e) {
////			// Handle other exceptions
////			response.put("error", e.getMessage());
////			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
////		}
////	}
//
//	private void updateSubmittedDocumentsStatus(InsurancePolicy policy) {
//		Set<SubmittedDocument> documents = policy.getDocuments();
//		for (SubmittedDocument document : documents) {
//			if (DocumentStatus.PENDING.name().equals(document.getDocumentStatus())) {
//				document.setDocumentStatus(DocumentStatus.APPROVED.name());
//				submittedDocumentRepository.save(document);
//			}
//		}
//	}
//
//	private void handleAgentCommission(Agent agent, InsurancePolicy policy, double amount) {
//		// Implement commission calculation and saving logic
//	}
//
////	@GetMapping("/customers/{customerId}/policy/{policyId}")
////	public ResponseEntity<InsurancePolicyDto> getPolicyById(@PathVariable("customerId") Long customerId,
////			@PathVariable("policyId") Long policyId) {
////		InsurancePolicyDto insurancePolicyDto = customerService.getPolicyByid(customerId, policyId);
////		return ResponseEntity.ok(insurancePolicyDto);
////	}
//
//	@GetMapping("/customers/{customerId}/policies")
//	public ResponseEntity<List<InsurancePolicyDto>> getAllPoliciesByCustomerId(
//			@PathVariable("customerId") Long customerId) {
//
//		try {
//			List<InsurancePolicyDto> policies = customerService.getAllPoliciesByCustomerId(customerId);
//			return ResponseEntity.ok(policies);
//		} catch (Exception e) {
//			// Log the exception (optional)
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
//		}
//	}
//
//}
