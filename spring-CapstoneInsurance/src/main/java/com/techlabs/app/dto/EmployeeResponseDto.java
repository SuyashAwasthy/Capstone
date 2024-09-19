package com.techlabs.app.dto;

import lombok.Data;

@Data
public class EmployeeResponseDto {

    private Long employeeId;
    private String name;
    private boolean isActive;

    private Long userId;
    private String username;
    private String email;

    //private Long addressId;
   // private String address;
  //  private Long pincode;

  //  private Long stateId;
    //private String stateName;

    private String lastName;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
    
    
    
    // Getters and Setters

   
   }
