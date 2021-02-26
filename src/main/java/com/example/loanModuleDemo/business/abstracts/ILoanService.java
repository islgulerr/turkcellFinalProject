package com.example.loanModuleDemo.business.abstracts;

import java.util.List;
import java.util.Optional;

import com.example.loanModuleDemo.entities.concretes.Loan;

public interface ILoanService {

	List<Loan> getAll();

	Loan add(Loan loan);

	String delete(Loan loan);

	Optional<Loan> findById(Integer loanId);

	List<Loan> findByCustomerId(int customerId);

}
