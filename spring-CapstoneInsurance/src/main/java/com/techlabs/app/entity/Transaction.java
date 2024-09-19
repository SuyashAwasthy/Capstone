//package com.techlabs.app.entity;
//
//import java.time.LocalDateTime;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name = "transactions")
//public class Transaction {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long transactionId;
//
//    @ManyToOne
//    @JoinColumn(name = "insurance_id", nullable = false)
//    private InsurancePolicy insurancePolicy;
//
//    @Column(nullable = false)
//    private String transactionType;  // e.g., PREMIUM_PAYMENT, CLAIM, WITHDRAWAL
//
//    @Column(nullable = false)
//    private Double amount;
//
//    @Column(nullable = false)
//    private LocalDateTime date;
//
//    private String status;  // e.g., SUCCESS, PENDING, FAILED
//
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}
//
//	public long getTransactionId() {
//		return transactionId;
//	}
//
//	public void setTransactionId(long transactionId) {
//		this.transactionId = transactionId;
//	}
//
//	public InsurancePolicy getInsurancePolicy() {
//		return insurancePolicy;
//	}
//
//	public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
//		this.insurancePolicy = insurancePolicy;
//	}
//
//	public String getTransactionType() {
//		return transactionType;
//	}
//
//	public void setTransactionType(String transactionType) {
//		this.transactionType = transactionType;
//	}
//
//	public Double getAmount() {
//		return amount;
//	}
//
//	public void setAmount(Double amount) {
//		this.amount = amount;
//	}
//
//	public LocalDateTime getDate() {
//		return date;
//	}
//
//	public void setDate(LocalDateTime date) {
//		this.date = date;
//	}
//	
//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}
//
//	private String type;
//	
//	@ManyToOne
//    @JoinColumn(name = "agent_id")
//    private Agent agent;
//
//    public void setAgent(Agent agent) {
//        this.agent = agent;
//    }
//	
//    @Column(name = "insurance_id")
//    private Long insuranceId; // Add this field
//
//	public Long getInsuranceId() {
//		return insuranceId;
//	}
//
//	public void setInsuranceId(Long insuranceId) {
//		this.insuranceId = insuranceId;
//	}
//
//	public Agent getAgent() {
//		return agent;
//	}
//    
//    
//	
//	
//	
//	
//    
//}


package com.techlabs.app.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;

    @ManyToOne
    @JoinColumn(name = "insurance_id", nullable = false)
    private InsurancePolicy insurancePolicy;

    @Column(nullable = false)
    private String transactionType;  // e.g., PREMIUM_PAYMENT, CLAIM, WITHDRAWAL

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime date;

    private String status;  // e.g., SUCCESS, PENDING, FAILED

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public InsurancePolicy getInsurancePolicy() {
		return insurancePolicy;
	}

	public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
		this.insurancePolicy = insurancePolicy;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String type;

    // No need for insuranceId field
    
    
}
