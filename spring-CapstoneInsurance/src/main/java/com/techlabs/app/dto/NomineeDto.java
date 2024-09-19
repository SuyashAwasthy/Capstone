package com.techlabs.app.dto;

import com.techlabs.app.entity.Nominee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NomineeDto {
	
	private String nomineeName;
	private String relationStatus;
	public NomineeDto(Nominee n) {
		// TODO Auto-generated constructor stub
	}
	public String getNomineeName() {
		return nomineeName;
	}
	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}
	public String getRelationStatus() {
		return relationStatus;
	}
	public void setRelationStatus(String relationStatus) {
		this.relationStatus = relationStatus;
	}
	
	

}
