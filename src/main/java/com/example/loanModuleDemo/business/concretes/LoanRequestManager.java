package com.example.loanModuleDemo.business.concretes;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.loanModuleDemo.business.abstracts.ILoanRequestService;
import com.example.loanModuleDemo.dataaccess.concretes.LoanRequestRepository;
import com.example.loanModuleDemo.entities.concretes.LoanRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoanRequestManager implements ILoanRequestService {

	@Autowired
	private LoanRequestRepository loanRequestRepository;

	@Override
	public List<LoanRequest> getAll() {

		return loanRequestRepository.findAll();
	}

	@Override
	public LoanRequest add(LoanRequest loanRequest) {
		return loanRequestRepository.save(loanRequest);
	}

	@Override
	public String delete(LoanRequest loanRequest) {
		loanRequestRepository.delete(loanRequest);
		return loanRequest + "silindi";
	}

	@Override
	public Optional<LoanRequest> findById(Integer requestId) {
		return loanRequestRepository.findById(requestId);
	}

	@Override
	public List<LoanRequest> findByCustomerId(int customerId) {
		return loanRequestRepository.findByCustomerId(customerId);
	}

	@Override
	public List<LoanRequest> findByFeedback(String feedback) {
		return loanRequestRepository.findByFeedback(feedback);
	}
}
