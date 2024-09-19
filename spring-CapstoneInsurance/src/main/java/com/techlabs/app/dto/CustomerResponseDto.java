//package com.techlabs.app.dto;
//
//import java.time.LocalDate;
//
//public class CustomerResponseDto {
//
//	public void setCityName(String city_name) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public void setFirstName(String firstName) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public void setActive(boolean active) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public void setLastName(String lastName) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public void setDob(LocalDate dob) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public void setPhoneNumber(long phoneNumber) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	
//	//policy list
//	// list of policy account response dto
//}


package com.techlabs.app.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class CustomerResponseDto {
    private long customerId;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private long phoneNumber;
    private String cityName; // Assuming you want to include city name
    private boolean isActive;
    private boolean verified;
    private LocalDate registrationDate;
    private String stateName;
    
    private String email;

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

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

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
    

    // You can include insurance policies if needed
    // private List<InsurancePolicyResponseDto> insurancePolicies;
}