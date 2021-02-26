package com.example.loanModuleDemo.business.concretes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.loanModuleDemo.api.controllers.dto.RegistrationRequest;
import com.example.loanModuleDemo.business.abstracts.IAuthorityService;
import com.example.loanModuleDemo.dataaccess.concretes.AuthorityRepository;
import com.example.loanModuleDemo.dataaccess.concretes.CustomerRepository;
import com.example.loanModuleDemo.dataaccess.concretes.LoanRepository;
import com.example.loanModuleDemo.dataaccess.concretes.LoanRequestRepository;
import com.example.loanModuleDemo.entities.concretes.Authority;
import com.example.loanModuleDemo.entities.concretes.Customer;
import com.example.loanModuleDemo.entities.concretes.Loan;
import com.example.loanModuleDemo.entities.concretes.LoanRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthorityManager implements IAuthorityService {

	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private LoanRequestRepository loanRequestRepository;

	@Override
	public List<Authority> getAll() {

		return authorityRepository.findAll();
	}

	@Override
	public Optional<Authority> findById(Integer authortiyId) {
		return authorityRepository.findById(authortiyId);
	}

	@Override
	public Authority add(Authority authority) {
		return authorityRepository.save(authority);
	}

	@Override
	public String delete(Authority authority) {
		authorityRepository.delete(authority);
		return authority + "silindi";
	}

	@Override
	public Double completeCustomerInfo(Integer customerId, RegistrationRequest request) throws Exception {
		Customer customer = customerRepository.findById(customerId).orElseThrow(
				() -> new Exception("Bu kimliğe maksimum kredi limiti belirlenmemiş müşteri yok : " + customerId));

		customer.setSubsDate(request.getSubsDate());
		customer.setBlackList(request.isBlackList());

		customerRepository.save(customer);

		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		LocalDate now = LocalDate.now();
		Date firstDate = dateformat.parse(customer.getSubsDate().toString());
		Date secondDate = dateformat.parse(now.toString());

		long differenceDay = secondDate.getTime() - firstDate.getTime();
		long totalDifferenceDay = TimeUnit.DAYS.convert(differenceDay, TimeUnit.MILLISECONDS);

		if (customer.isBlackList() != true) {
			if (totalDifferenceDay > 730) {
				customer.setLoanLimit(50000);
			} else if (totalDifferenceDay < 730 && totalDifferenceDay > 500) {
				customer.setLoanLimit(20000);
			} else if (1 < totalDifferenceDay && totalDifferenceDay > 365) {
				customer.setLoanLimit(5000);
			}
		} else {
			customer.setLoanLimit(0);
		}

		customerRepository.save(customer);

		return customer.getLoanLimit();
	}

	@Override
	public String updateBlacklist(Integer customerId, RegistrationRequest request) throws Exception {
		Customer customer = customerRepository.getOne(customerId);
		customer.setBlackList(request.isBlackList());

		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		LocalDate now = LocalDate.now();
		Date firstDate = dateformat.parse(customer.getSubsDate().toString());
		Date secondDate = dateformat.parse(now.toString());

		long differenceDay = secondDate.getTime() - firstDate.getTime();
		long totalDifferenceDay = TimeUnit.DAYS.convert(differenceDay, TimeUnit.MILLISECONDS);

		if (customer.isBlackList() != true) {
			if (totalDifferenceDay > 730) {
				customer.setLoanLimit(50000);
			} else if (totalDifferenceDay < 730 && totalDifferenceDay > 500) {
				customer.setLoanLimit(20000);
			} else if (1 < totalDifferenceDay && totalDifferenceDay > 365) {
				customer.setLoanLimit(5000);
			}
		} else {
			customer.setLoanLimit(0);
		}

		return "Limit güncellendi";

	}

	@Override
	public String confirmRequestLoan(Integer requestId) throws Exception {
		LoanRequest loanRequest = loanRequestRepository.findById(requestId).orElseThrow(
				() -> new Exception("Bu kimliğe sahip bir kredi giriş isteği bulunmamaktadır. : " + requestId));

		Customer customer = customerRepository.findById(loanRequest.getCustomerId()).orElseThrow(
				() -> new Exception("Bu kimliğe sahip, kredi giriş isteğinde bulunmuş bir müşteri bulunamadı : "
						+ loanRequest.getCustomerId()));
		;

		Loan loan = new Loan();

		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		LocalDate now = LocalDate.now();
		Date confirmLoanDate = dateformat.parse(now.toString());

		int size = 0;
		for (Loan instant : loanRepository.findAll()) {
			if (size > 12) {

				throw new Exception("1 yıldaki kredi hakkını doldurdu.");

			}

			if (loanRequest.getId() == instant.getCustomerId()) {
				size += 1;
			}
		}

		if (loanRequest.getLoanDocument() != null && loanRequest.getAmount() <= customer.getLoanLimit()) {
			loanRequest.setFeedback("onaylandı");
			loan.setCustomerId(customer.getId());
			loan.setAmount(loanRequest.getAmount());
			loan.setDrawdownDate(confirmLoanDate);
			loan.setLoanDocument(loanRequest.getLoanDocument());
			loanRepository.save(loan);
		} else {
			loanRequest.setFeedback("reddedildi");
		}

		loanRequestRepository.save(loanRequest);

		return loanRequest.getFeedback();
	}

}
