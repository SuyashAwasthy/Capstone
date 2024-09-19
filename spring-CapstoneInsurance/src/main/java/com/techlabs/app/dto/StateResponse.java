package com.techlabs.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class StateResponse {
	
	private Long stateId;
    private String name;
    private Boolean isActive;
    private List<CityResponse> cities;
	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public List<CityResponse> getCities() {
		return cities;
	}
	public void setCities(List<CityResponse> cities) {
		this.cities = cities;
	}
    
    


}
