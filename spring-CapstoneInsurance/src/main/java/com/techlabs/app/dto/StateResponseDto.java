package com.techlabs.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class StateResponseDto {

    private Long stateId;
    private String name;
    private List<CityResponseDto> cities;  // Assuming you want to include cities in the state response
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
	public List<CityResponseDto> getCities() {
		return cities;
	}
	public void setCities(List<CityResponseDto> cities) {
		this.cities = cities;
	}
    
    

    // You can include additional fields if needed
}
