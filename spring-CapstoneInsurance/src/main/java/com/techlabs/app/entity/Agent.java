//package com.techlabs.app.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import java.util.Set;
//
//@Entity
//@Data
//@Table(name = "agents")
//public class Agent {
//
//	@Id
//	 @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private long agentId;
//
//	@OneToOne
//	@JoinColumn(name = "user_id", nullable = false)
//	private User user;
//
//	private String firstName;
//	private String lastName;
//	private String phoneNumber;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "city_id")
//	private City city;
//
////    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
////    private Set<Customer> customers;
//
//	private boolean isActive;
//
//	@OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
//	private Set<Commission> commissions;
//
//	private boolean verified = false;
//	
//	 private double totalCommission;
//
////	public void setTotalCommission(double totalCommission) {
////		// TODO Auto-generated method stub
////		
////	}
////
////	public double getTotalCommission() {
////		// TODO Auto-generated method stub
////		return 0;
////	}
//
////	@OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
////	private Set<InsurancePolicy> insurancePolicies = new HashSet<>();
//
//}

package com.techlabs.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@Table(name = "agents")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long agentId;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "city_id")
    private City city;

    private boolean isActive;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("agent")
    private Set<Commission> commissions;
    
    
    private double totalCommission;
    
    private boolean verified = false;
    
    @Column(name = "registration_date") 
    private LocalDate registrationDate;

	public long getAgentId() {
		return agentId;
	}

	public void setAgentId(long agentId) {
		this.agentId = agentId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Set<Commission> getCommissions() {
		return commissions;
	}

	public void setCommissions(Set<Commission> commissions) {
		this.commissions = commissions;
	}

	public double getTotalCommission() {
		return totalCommission;
	}

	public void setTotalCommission(double totalCommission) {
		this.totalCommission = totalCommission;
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
	
	private String password; 

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    

}

