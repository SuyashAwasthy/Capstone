package com.techlabs.app.dto;

import java.util.List;
import java.util.Set;

import com.techlabs.app.entity.City;
import com.techlabs.app.entity.Commission;
import com.techlabs.app.entity.Customer;

import lombok.Data;

@Data
public class AgentResponseDto {

	private Long agentId;

	private UserResponseDto userResponseDto;

	private String name;

	private String phoneNumber;

	private City city;
	private CityResponseDto cityResponseDto;

//	private Set<Customer> customers;
	private List<Customer> customers;


	private boolean isActive;

//	private Set<Commission> commissions;
	private List<Commission> commissions;

	public AgentResponseDto(long agentId2, String firstName, String lastName, String phoneNumber2) {
		// TODO Auto-generated constructor stub
	}

	public AgentResponseDto() {
		// TODO Auto-generated constructor stub
	}

	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	public UserResponseDto getUserResponseDto() {
		return userResponseDto;
	}

	public void setUserResponseDto(UserResponseDto userResponseDto) {
		this.userResponseDto = userResponseDto;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public CityResponseDto getCityResponseDto() {
		return cityResponseDto;
	}

	public void setCityResponseDto(CityResponseDto cityResponseDto) {
		this.cityResponseDto = cityResponseDto;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<Commission> getCommissions() {
		return commissions;
	}

	public void setCommissions(List<Commission> commissions) {
		this.commissions = commissions;
	}
	
	
	
	 

}
