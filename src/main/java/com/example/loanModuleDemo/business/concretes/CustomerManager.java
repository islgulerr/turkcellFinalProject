package com.example.loanModuleDemo.business.concretes;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.loanModuleDemo.api.controllers.dto.RegistrationRequest;
import com.example.loanModuleDemo.business.abstracts.ICustomerService;
import com.example.loanModuleDemo.dataaccess.concretes.CustomerRepository;
import com.example.loanModuleDemo.dataaccess.concretes.LoanRequestRepository;
import com.example.loanModuleDemo.entities.concretes.Customer;
import com.example.loanModuleDemo.entities.concretes.LoanRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerManager implements ICustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private LoanRequestRepository loanRequestRepository;

	@Override
	public List<Customer> getAll() {

		return customerRepository.findAll();
	}

	@Override
	public Customer add(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	public Integer register(RegistrationRequest request) throws Exception {

		Customer customer = new Customer();

		customer.setIdentityNo(request.getIdentityNo());
		customer.setCustomerName(request.getCustomerName());
		customer.setCustomerSurname(request.getCustomerSurname());
		customer.setGsmNo(request.getGsmNo());
		customer.setAddress(request.getAddress());
		customer.setBirthDate(request.getBirthDate());
		customer.setCustomerType(request.getCustomerType());

		customerRepository.save(customer);

		return customer.getId();
	}

	@Override
	public Integer identityDocumentload(Integer customerId, String identityDocument) throws Exception {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new Exception("Bu kimliğe sahip kayıt aşamasında müşteri yok : " + customerId));

		customer.setIdentityDocument(identityDocument);

		customerRepository.save(customer);

		return customer.getId();
	}

	@Override
	public Integer requestLoan(Integer customerId, RegistrationRequest request) throws Exception {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new Exception("Bu kimliğe sahip müşteri yok : " + customerId));

		LoanRequest loanRequest = new LoanRequest();

		int size = 0;
		for (LoanRequest instant : loanRequestRepository.findAll()) {
			if (size > 0) {

				throw new Exception("Ayda sadece 1 kredi hakkınız var.");

			}

			if (customerId == instant.getCustomerId()) {
				size += 1;
			}
		}

		if (customer.getIdentityDocument() == null || customer.getLoanLimit() == 0) {
			return 0;
		} else {

			loanRequest.setCustomerId(customerId);
			loanRequest.setAmount(request.getAmount());
			loanRequest.setLoanDocument(request.getLoanDocument());

		}

		loanRequestRepository.save(loanRequest);

		return loanRequest.getId();
	}

	@Override
	public Integer updateRequestLoan(Integer requestId, RegistrationRequest request) throws Exception {
		LoanRequest loanRequest = loanRequestRepository.findById(requestId)
				.orElseThrow(() -> new Exception("Bu kimliğe sahip kredi girişi yok : " + requestId));

		loanRequest.setFeedback(null);
		loanRequest.setAmount(request.getAmount());
		loanRequest.setLoanDocument(request.getLoanDocument());
		loanRequestRepository.save(loanRequest);

		return loanRequest.getId();
	}

	@Override
	public Optional<Customer> findById(Integer customerId) {
		return customerRepository.findById(customerId);
	}

	@Override
	public Customer findById(String identityNo) {
		return customerRepository.findByIdentityNo(identityNo);
	}

}
