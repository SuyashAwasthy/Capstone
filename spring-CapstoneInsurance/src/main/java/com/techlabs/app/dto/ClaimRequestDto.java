package com.techlabs.app.dto;
//
//import lombok.Data;
//
//@Data
//public class ClaimRequestDto {
//	private Long policyId;
//	 private String ifscCode;
//	 private String branchName;
//	 private String bankAccountId;
//	 private String bankName;
//private Double claimAmount;
//
//}
import lombok.Data;

@Data
public class ClaimRequestDto {
    private Long policyId;
    private String ifscCode;
    private String branchName;
    private String bankAccountId;
    private String bankName;
    private Double claimAmount; // New field for the claim amount
	public Long getPolicyId() {
		return policyId;
	}
	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBankAccountId() {
		return bankAccountId;
	}
	public void setBankAccountId(String bankAccountId) {
		this.bankAccountId = bankAccountId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Double getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}
    
    
    
    

}
