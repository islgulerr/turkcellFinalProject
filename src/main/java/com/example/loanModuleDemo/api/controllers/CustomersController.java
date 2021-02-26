package com.example.loanModuleDemo.api.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.loanModuleDemo.api.controllers.dto.RegistrationRequest;
import com.example.loanModuleDemo.business.abstracts.ICustomerService;
import com.example.loanModuleDemo.business.abstracts.ILoanRequestService;
import com.example.loanModuleDemo.business.abstracts.ILoanService;
import com.example.loanModuleDemo.entities.concretes.Customer;
import com.example.loanModuleDemo.entities.concretes.Loan;
import com.example.loanModuleDemo.entities.concretes.LoanRequest;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CustomersController {
	
	@Autowired
	private ILoanService loanService;
	
	@Autowired
	private ICustomerService customerService;

	@Autowired
	private ILoanRequestService loanRequestService;
	

	@PostMapping("/registration")
	public Map<String, Boolean> newCustomerRegister(@RequestBody RegistrationRequest request) throws Exception {

		Customer customer = customerService.findById(request.getIdentityNo());

		
		Map<String, Boolean> response = new HashMap<>();

		if (customer == null) {

			Integer customerId = customerService.register(request);

			response.put("Müşteri id: "+customerId
					+ " Kimlik ve kişisel bilgileriniz başarıyla eklendi. Kaydınızın tamamlanması için lütfen kimlik belgenizi sistemimize yükleyiniz.",
					Boolean.TRUE);
			return response;
		} else {
			response.put(request.getIdentityNo() + " kimlikli kullanıcının aboneliği bulunmaktadır. Direkt giriş yapabilirsiniz.", Boolean.FALSE);
			return response;

		}
	}

	@PostMapping("/registration/loadfile/{id}")
	public Map<String, Boolean> loadIdentityDocument(@PathVariable("id") Integer customerId, @RequestBody RegistrationRequest request)
			throws Exception {
		Customer customer = customerService.findById(customerId).orElseThrow(
				() -> new Exception("Bu kimliğe sahip kayıt aşamasında müşteri bulunmamaktadır. : " + customerId));

		customer.setIdentityDocument(request.getIdentityDocument());

		Integer customerId1 = customerService.identityDocumentload(customerId, customer.getIdentityDocument());

		Map<String, Boolean> response = new HashMap<>();
		response.put("Sayın " + customer.getCustomerName()
				+ ", kaydınız başarıyla tamamlandı. Kredi talebinde bulunabilirsiniz.", Boolean.TRUE);
		return response;
	}

	@GetMapping("/customerinformation/{id}")
	public Map<String, Boolean> showCustomerInformation(@PathVariable("id") Integer customerId) throws Exception {
		Customer customer = customerService.findById(customerId).orElseThrow(
				() -> new Exception("Bu kimliğe sahip kayıt aşamasında müşteri bulunmamaktadır. : " + customerId));

		Map<String, Boolean> response = new HashMap<>();
		response.put(customer + " Kullanıcı bilgileriniz başarıyla getirildi.", Boolean.TRUE);
		return response;
	}

	@PostMapping("/loanrequest/{id}")
	public Map<String, Boolean> requestCustomerLoan(@PathVariable("id") Integer customerId,
			@RequestBody RegistrationRequest request) throws Exception {
		Customer customer = customerService.findById(customerId).orElseThrow(
				() -> new Exception("Bu kimliğe sahip müşteri bulunmamaktadır. Lütfen kayıt yaptırınız : " + customerId));

		
		Map<String, Boolean> response = new HashMap<>();

		if (customer.getIdentityDocument()== null || customer.getLoanLimit() == 0) {
			response.put("Sayın " + customer.getCustomerName()
					+ ", kredi girişiniz kimlik belgesi eksikliği ya da limit yetersizliğinden ötürü tamamlanmadı. Lütfen kimlik belgenizi yükleyiniz ya da yetkililerimizle iletişime geçiniz.",
					Boolean.FALSE);
			return response;
		} else {

			customerService.requestLoan(customerId, request);

			response.put("Sayın " + customer.getCustomerName()
					+ ", kredi girişiniz tamamlandı. Yetkilelerimiz tarafından talebinizle ilgili geri dönüş yapılacaktır. ",
					Boolean.TRUE);
			return response;
		}

	}
	
	@PostMapping("/loanrequest/updateloanrequest/{id}/{rid}")
	public Map<String, Boolean> updateRequestCustomerLoan(@PathVariable("id") Integer customerId,@PathVariable("rid") Integer requestId,
			@RequestBody RegistrationRequest request) throws Exception {
		
		Customer customer = customerService.findById(customerId).orElseThrow(
				() -> new Exception("Bu kimliğe sahip müşteri bulunmamaktadır. Lütfen kayıt yaptırınız : " + customerId));

		Map<String, Boolean> response = new HashMap<>();
		
		customerService.updateRequestLoan(requestId, request);

		response.put("Sayın " + customer.getCustomerName()+ ", kredi girişiniz güncellendi. Yetkilelerimiz tarafından talebinizle ilgili geri dönüş yapılacaktır. ",
					Boolean.TRUE);
			return response;
		}

	

	@GetMapping("/loanrequest/{id}")
	public Map<String, Boolean> getCustomerRequestLoan(@PathVariable("id") Integer customerId) throws Exception {
		Customer customer = customerService.findById(customerId).orElseThrow(
				() -> new Exception("Bu kimliğe sahip kayıt aşamasında müşteri bulunmamaktadır. : " + customerId));
		
		List<LoanRequest> requestedLoanGetAll = loanRequestService.findByCustomerId(customerId);

		List<String> temporaryList = new ArrayList<>();
		for(int i=0; i<requestedLoanGetAll.size();i++) {
			temporaryList.add("[");
			temporaryList.add("Kredi girişinizin numarası : " + String.valueOf(requestedLoanGetAll.get(i).getId()));
			temporaryList.add("Kredi girişinizde talep ettiğiniz tutar: "+String.valueOf(requestedLoanGetAll.get(i).getAmount()));
			temporaryList.add("Kredi girişinizin onay durumu: " +requestedLoanGetAll.get(i).getFeedback());
			temporaryList.add("]");
		}
		Map<String, Boolean> response = new HashMap<>();
		response.put(temporaryList + "Sayın," + customer.getCustomerName() + " "
				+ customer.getCustomerSurname()
				+ ", kredi giriş bilgileriniz listelendi. Reddedilen kredi girişini güncelledikten sonra onay için bekleyiniz.Kredi limitinize müşteri bilgileri sayfasından erişebilirsiniz.",
				Boolean.TRUE);
		return response;
	}
	
	@GetMapping("/loan/{id}")
	public Map<String, Boolean> getCustomerLoan(@PathVariable("id") Integer customerId) throws Exception {
		Customer customer = customerService.findById(customerId).orElseThrow(
				() -> new Exception("Bu kimliğe sahip kayıt aşamasında müşteri bulunmamaktadır. : " + customerId));
		
		List<Loan> loanGetAll = loanService.findByCustomerId(customerId);
		
		List<String> temporaryList = new ArrayList<>();
		for(int i=0; i<loanGetAll.size();i++) {
			temporaryList.add("[");
			temporaryList.add("Onaylı kredinizin kredi idsi:  "+String.valueOf(loanGetAll.get(i).getId()));
			temporaryList.add("Onaylı kredinizin tutarı: "+String.valueOf(loanGetAll.get(i).getAmount()));
			temporaryList.add("Onaylı kredinizin onay tarihi:  " +String.valueOf(loanGetAll.get(i).getDrawdownDate()));
			temporaryList.add("]");
		}
		
		
		Map<String, Boolean> response = new HashMap<>();
		if(temporaryList.size() != 0) {
			response.put(temporaryList+ "Sayın," + customer.getCustomerName() + " "
					+ customer.getCustomerSurname()
					+ ", onaylı kredi bilgileriniz getirildi.",
					Boolean.TRUE);
			return response;
		} else {
			response.put("Sayın," + customer.getCustomerName() + " " + customer.getCustomerSurname() + ", onaylı krediler listesinde size ait bir kredi bilgisi bulunamadı.", Boolean.FALSE);
			return response;
		}

		
		
	}

}