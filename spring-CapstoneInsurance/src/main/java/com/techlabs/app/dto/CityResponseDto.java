package com.techlabs.app.dto;

import lombok.Data;

@Data
public class CityResponseDto {

    private Long id;
    private String cityName;
    private StateResponseDto state;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public StateResponseDto getState() {
		return state;
	}
	public void setState(StateResponseDto state) {
		this.state = state;
	}
    
    

}
