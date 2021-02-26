package com.example.loanModuleDemo.dataaccess.concretes;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.loanModuleDemo.entities.concretes.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	public Object findById(byte[] identityDocument);

	public Customer findByIdentityNo(String identityNo);

}
