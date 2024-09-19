package com.techlabs.app.repository;

import com.techlabs.app.entity.InsurancePolicy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {

//	List<InsurancePolicy> findByCustomersId(Long customerId);
	
	// Custom query to find policies by customer ID
    @Query("SELECT p FROM InsurancePolicy p JOIN p.customers c WHERE c.id = :customerId")
    List<InsurancePolicy> findByCustomerId(@Param("customerId") Long customerId);

//	InsurancePolicy findByCustomerIdAndPolicyId(Long customerId, Long policyId);

	//List<InsurancePolicy> findByCustomersCustomerId(Long customerId);
	
//    InsurancePolicy findByCustomers_IdAndPolicyId(Long customerId, Long policyId);

}