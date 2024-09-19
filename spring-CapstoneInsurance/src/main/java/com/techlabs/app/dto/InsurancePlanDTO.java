package com.techlabs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.techlabs.app.entity.InsurancePlan;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePlanDTO {
//	private long insurancePlanId;
//	private String name;
//	private boolean active;
//	private List<InsuranceSchemeDto> insuranceSchemes;
//
//	public InsurancePlanDTO(long insurancePlanId2, String name2, boolean active2) {
//		this.insurancePlanId = insurancePlanId2;
//		this.name = name2;
//		this.active = active2;
//	}
//
//	public InsurancePlanDTO(long insurancePlanId, String name, boolean active,
//			List<InsuranceSchemeDto> insuranceSchemes) {
//		super();
//		this.insurancePlanId = insurancePlanId;
//		this.name = name;
//		this.active = active;
//		this.insuranceSchemes = insuranceSchemes;
//	}
//
//	public InsurancePlanDTO(InsurancePlan insurancePlan) {
//		// TODO Auto-generated constructor stub
//	}
//
////	public InsurancePlanDTO(long insurancePlanId2, String name2, boolean active2) {
////		// TODO Auto-generated constructor stub
////	}
//	public long getInsurancePlanId() {
//		return insurancePlanId;
//	}
//
//	public void setInsurancePlanId(long insurancePlanId) {
//		this.insurancePlanId = insurancePlanId;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public boolean isActive() {
//		return active;
//	}
//
//	public void setActive(boolean active) {
//		this.active = active;
//	}
//
//	public List<InsuranceSchemeDto> getInsuranceSchemes() {
//		return insuranceSchemes;
//	}
//
//	public void setInsuranceSchemes(List<InsuranceSchemeDto> insuranceSchemes) {
//		this.insuranceSchemes = insuranceSchemes;
//	}

	public InsurancePlanDTO(InsurancePlan plan) {

	}

	public InsurancePlanDTO(long insurancePlanId2, String name2, boolean active2) {
		this.insurancePlanId = insurancePlanId2;
		this.name = name2;
		this.active = active2;
	}

	private long insurancePlanId;
	private String name;
	private boolean active;
	private List<InsuranceSchemeDto> insuranceSchemes;
	public long getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(long insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<InsuranceSchemeDto> getInsuranceSchemes() {
		return insuranceSchemes;
	}

	public void setInsuranceSchemes(List<InsuranceSchemeDto> insuranceSchemes) {
		this.insuranceSchemes = insuranceSchemes;
	}
	
	
}
