package com.example.loanModuleDemo.dataaccess.concretes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.loanModuleDemo.entities.concretes.Loan;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

	public List<Loan> findByCustomerId(int customerId);
}
