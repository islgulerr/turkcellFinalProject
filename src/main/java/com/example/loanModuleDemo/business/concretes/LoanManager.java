package com.example.loanModuleDemo.business.concretes;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.loanModuleDemo.business.abstracts.ILoanService;
import com.example.loanModuleDemo.dataaccess.concretes.LoanRepository;
import com.example.loanModuleDemo.entities.concretes.Loan;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoanManager implements ILoanService {

	@Autowired
	private LoanRepository loanRepository;

	@Override
	public List<Loan> getAll() {

		return loanRepository.findAll();
	}

	@Override
	public Loan add(Loan loan) {
		return loanRepository.save(loan);
	}

	@Override
	public String delete(Loan loan) {
		loanRepository.delete(loan);
		return loan + "silindi";
	}

	@Override
	public Optional<Loan> findById(Integer loanId) {
		return loanRepository.findById(loanId);
	}

	@Override
	public List<Loan> findByCustomerId(int customerId) {
		return loanRepository.findByCustomerId(customerId);
	}
}
