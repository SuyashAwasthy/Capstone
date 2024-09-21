package com.techlabs.app.repository;

import com.techlabs.app.entity.InsurancePolicy;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
//    @Query("SELECT i FROM InsurancePolicy i " +
//            "JOIN i.customer c " +
//            "JOIN c.user u " +
//            "JOIN i.insuranceScheme s " +
//            "WHERE (:id IS NULL OR i.id = :id) " +
//            "AND (:customerId IS NULL OR c.id = :customerId) " +
//            "AND (:agentId IS NULL OR i.agent.id = :agentId) " +
//            "AND (:schemeId IS NULL OR s.id = :schemeId) " +
//            "AND (:name IS NULL OR LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))) " +
//            "AND (:schemeName IS NULL OR LOWER(s.schemeName) LIKE LOWER(CONCAT('%', :schemeName, '%'))) " +
//            "AND (:policyStatus IS NULL OR i.policyStatus = :policyStatus)")
//    Page<InsurancePolicy> findAllPoliciesBasedOnSearch(@Param("id") Long id,
//                                                       @Param("customerId") Long customerId,
//                                                       @Param("agentId") Long agentId,
//                                                       @Param("schemeId") Long schemeId,
//                                                       @Param("schemeName") String schemeName,
//                                                       @Param("name") String name,
//                                                       @Param("policyStatus") String policyStatus,
//                                                       Pageable pageable);
    
    @Query("SELECT i FROM InsurancePolicy i " +
    	       "JOIN i.customers c " +
    	       "JOIN c.user u " +
    	       "JOIN i.insuranceScheme s " +
    	       "WHERE (:id IS NULL OR i.insuranceId = :id) " +
    	       "AND (:customerId IS NULL OR c.id = :customerId) " +
    	       "AND (:agentId IS NULL OR i.agent.id = :agentId) " +
    	       "AND (:schemeId IS NULL OR s.id = :schemeId) " +
    	       "AND (:name IS NULL OR LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))) " +
    	       "AND (:schemeName IS NULL OR LOWER(s.schemeName) LIKE LOWER(CONCAT('%', :schemeName, '%'))) " +
    	       "AND (:policyStatus IS NULL OR i.policyStatus = :policyStatus)")
    	Page<InsurancePolicy> findAllPoliciesBasedOnSearch(@Param("id") Long id,
    	                                                   @Param("customerId") Long customerId,
    	                                                   @Param("agentId") Long agentId,
    	                                                   @Param("schemeId") Long schemeId,
    	                                                   @Param("schemeName") String schemeName,
    	                                                   @Param("name") String name,
    	                                                   @Param("policyStatus") String policyStatus,
    	                                                   Pageable pageable);


}