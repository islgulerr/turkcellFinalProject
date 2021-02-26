package com.example.loanModuleDemo.business.abstracts;

import java.util.List;
import java.util.Optional;

import com.example.loanModuleDemo.entities.concretes.LoanRequest;

public interface ILoanRequestService {

	List<LoanRequest> getAll();

	String delete(LoanRequest loanRequest);

	LoanRequest add(LoanRequest loanRequest);

	List<LoanRequest> findByFeedback(String feedback);

	Optional<LoanRequest> findById(Integer requestId);

	List<LoanRequest> findByCustomerId(int customerId);
}
