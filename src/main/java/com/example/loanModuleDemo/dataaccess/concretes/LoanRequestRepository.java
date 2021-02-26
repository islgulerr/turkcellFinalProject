package com.example.loanModuleDemo.dataaccess.concretes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.loanModuleDemo.entities.concretes.LoanRequest;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Integer> {

	public List<LoanRequest> findByCustomerId(int customerId);

	public List<LoanRequest> findByFeedback(String feedback);

}
