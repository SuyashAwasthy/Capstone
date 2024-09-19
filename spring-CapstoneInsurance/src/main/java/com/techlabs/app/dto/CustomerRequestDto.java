//package com.techlabs.app.dto;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import lombok.Data;
//
//@Data
//public class CustomerRequestDto {
//	
//	private String firstName; 
//    private String lastName; 
//    private LocalDate dob; 
//    private String phoneNumber; 
//    private Long cityId; // Assuming you will pass city ID 
//    private List<Long> insurancePolicyIds; // Assuming you will pass insurance policy IDs 
//	public String getFirstName() {
//		return firstName;
//	}
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//	public String getLastName() {
//		return lastName;
//	}
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//	public LocalDate getDob() {
//		return dob;
//	}
//	public void setDob(LocalDate dob) {
//		this.dob = dob;
//	}
//	public String getPhoneNumber() {
//		return phoneNumber;
//	}
//	public void setPhoneNumber(String phoneNumber) {
//		this.phoneNumber = phoneNumber;
//	}
//	public Long getCityId() {
//		return cityId;
//	}
//	public void setCityId(Long cityId) {
//		this.cityId = cityId;
//	}
//	public List<Long> getInsurancePolicyIds() {
//		return insurancePolicyIds;
//	}
//	public void setInsurancePolicyIds(List<Long> insurancePolicyIds) {
//		this.insurancePolicyIds = insurancePolicyIds;
//	}
//	public Object getIsActive() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//    
//    
//
//}

package com.techlabs.app.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class CustomerRequestDto {
	
	private String firstName; 
    private String lastName; 
    private LocalDate dob; 
    private long phoneNumber; 
    private Long cityId; // Assuming you will pass city ID 
    private List<Long> insurancePolicyIds; // Assuming you will pass insurance policy IDs 
    private Boolean isActive; // Example of a filter parameter
    private Boolean verified; // Example of a filter parameter
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public LocalDate getDob() {
		return dob;
	}
	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public List<Long> getInsurancePolicyIds() {
		return insurancePolicyIds;
	}
	public void setInsurancePolicyIds(List<Long> insurancePolicyIds) {
		this.insurancePolicyIds = insurancePolicyIds;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getVerified() {
		return verified;
	}
	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
    
    

}
