package com.example.loanModuleDemo.business.abstracts;

import java.util.List;
import java.util.Optional;

import com.example.loanModuleDemo.api.controllers.dto.RegistrationRequest;
import com.example.loanModuleDemo.entities.concretes.Customer;

public interface ICustomerService {

	List<Customer> getAll();

	Customer add(Customer customer);

	public Customer findById(String identityNo);

	Optional<Customer> findById(Integer customerId);

	public Integer register(RegistrationRequest request) throws Exception;

	Integer requestLoan(Integer customerId, RegistrationRequest request) throws Exception;

	Integer identityDocumentload(Integer customerId, String identityDocument) throws Exception;

	Integer updateRequestLoan(Integer customerId, RegistrationRequest request) throws Exception;

}
