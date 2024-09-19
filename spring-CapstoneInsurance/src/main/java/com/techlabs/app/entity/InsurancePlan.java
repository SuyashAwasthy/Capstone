package com.techlabs.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.techlabs.app.dto.InsuranceSchemeDto;

@Entity
@Data
@Table(name = "insurance_plan")
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePlan {

    public InsurancePlan(long insurancePlanId2) {

this.insurancePlanId=insurancePlanId2;
	}



	public InsurancePlan() {
		// TODO Auto-generated constructor stub
	}



	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long insurancePlanId;

    @Column(nullable = false)
    private String name;
    
    private boolean active;


    
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "insurancePlan", cascade = CascadeType.ALL)
    private List<InsuranceScheme> insuranceSchemes;



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



	public List<InsuranceScheme> getInsuranceSchemes() {
		return insuranceSchemes;
	}



	public void setInsuranceSchemes(List<InsuranceScheme> insuranceSchemes) {
		this.insuranceSchemes = insuranceSchemes;
	}
    
    



	

	
	
    
}