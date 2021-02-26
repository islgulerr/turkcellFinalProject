package com.example.loanModuleDemo.business.abstracts;

import java.util.List;
import java.util.Optional;

import com.example.loanModuleDemo.api.controllers.dto.RegistrationRequest;
import com.example.loanModuleDemo.entities.concretes.Authority;

public interface IAuthorityService {

	List<Authority> getAll();

	public Authority add(Authority authority);

	public String delete(Authority authority);

	public Optional<Authority> findById(Integer authorityId);

	String confirmRequestLoan(Integer requestId) throws Exception;

	Double completeCustomerInfo(Integer customerId, RegistrationRequest request) throws Exception;

	public String updateBlacklist(Integer customerId, RegistrationRequest request) throws Exception;

}
