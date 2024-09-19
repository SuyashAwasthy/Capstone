package com.techlabs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePolicyDto {
   
	public InsurancePolicyDto(Long insuranceId2, long l, long m, long n, List<Long> nomineeIds2, List<Long> paymentIds2,
			Set<Long> documentIds2, List<Long> customerIds2, LocalDate issuedDate2, LocalDate maturityDate2,
			double premiumAmount2, String policyStatus2, boolean active2) {
		
	}

	public InsurancePolicyDto() {
		// TODO Auto-generated constructor stub
	}

	private Long insuranceId;
    private Long insuranceSchemeId; // Refers to the associated scheme
    private Long agentId; // Refers to the associated agent
    private Long claimId; // Refers to the associated claim
   
    private List<NomineeDto> nominees;
    private List<Long> nomineeIds; // List of nominee IDs
    private List<Long> paymentIds; // List of payment IDs
    private Set<Long> documentIds; // Set of document IDs
    private List<Long> customerIds; // List of customer IDs
    private LocalDate issuedDate;
    private LocalDate maturityDate;
    private Double premiumAmount;
    private String policyStatus;
    private boolean active;
    private int policyTerm;
    private List<SubmittedDocumentDto> documents;  // Updated to include document details

    private int installmentPeriod;

	public Long getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Long insuranceId) {
		this.insuranceId = insuranceId;
	}

	public Long getInsuranceSchemeId() {
		return insuranceSchemeId;
	}

	public void setInsuranceSchemeId(Long insuranceSchemeId) {
		this.insuranceSchemeId = insuranceSchemeId;
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public Long getClaimId() {
		return claimId;
	}

	public void setClaimId(Long claimId) {
		this.claimId = claimId;
	}

	public List<NomineeDto> getNominees() {
		return nominees;
	}

	public void setNominees(List<NomineeDto> nominees) {
		this.nominees = nominees;
	}

	public List<Long> getNomineeIds() {
		return nomineeIds;
	}

	public void setNomineeIds(List<Long> nomineeIds) {
		this.nomineeIds = nomineeIds;
	}

	public List<Long> getPaymentIds() {
		return paymentIds;
	}

	public void setPaymentIds(List<Long> paymentIds) {
		this.paymentIds = paymentIds;
	}

	public Set<Long> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(Set<Long> documentIds) {
		this.documentIds = documentIds;
	}

	public List<Long> getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(List<Long> customerIds) {
		this.customerIds = customerIds;
	}

	public LocalDate getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(LocalDate issuedDate) {
		this.issuedDate = issuedDate;
	}

	public LocalDate getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(LocalDate maturityDate) {
		this.maturityDate = maturityDate;
	}

	public Double getPremiumAmount() {
		return premiumAmount;
	}

	public void setPremiumAmount(Double premiumAmount) {
		this.premiumAmount = premiumAmount;
	}

	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getPolicyTerm() {
		return policyTerm;
	}

	public void setPolicyTerm(int policyTerm) {
		this.policyTerm = policyTerm;
	}

	public List<SubmittedDocumentDto> getDocuments() {
		return documents;
	}

	public void setDocuments(List<SubmittedDocumentDto> documents) {
		this.documents = documents;
	}

	public int getInstallmentPeriod() {
		return installmentPeriod;
	}

	public void setInstallmentPeriod(int installmentPeriod) {
		this.installmentPeriod = installmentPeriod;
	}

	public void setInsuranceScheme(Object object) {
		// TODO Auto-generated method stub
		
	}
    
    


	

	
}
//private long insuranceId;
//private long insuranceSchemeId;
//private long agentId;
//private long claimId;
//private List<NomineeDto> nominees;
//private List<Long> paymentIds;
//private Set<Long> documentIds;  // If you still want to keep the document IDs
//private List<SubmittedDocumentDto> documents;  // Updated to include document details
//private List<Long> customerIds;
//
//private double premiumAmount;
//private int policyTerm;
//private int installmentPeriod;


