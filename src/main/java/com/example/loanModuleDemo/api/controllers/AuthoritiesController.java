package com.example.loanModuleDemo.api.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.loanModuleDemo.api.controllers.dto.RegistrationRequest;
import com.example.loanModuleDemo.business.abstracts.IAuthorityService;
import com.example.loanModuleDemo.business.abstracts.ICustomerService;
import com.example.loanModuleDemo.business.abstracts.ILoanRequestService;
import com.example.loanModuleDemo.entities.concretes.Authority;
import com.example.loanModuleDemo.entities.concretes.Customer;
import com.example.loanModuleDemo.entities.concretes.LoanRequest;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AuthoritiesController {

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private IAuthorityService authorityService;

	@Autowired
	private ILoanRequestService loanRequestService;

	@GetMapping("/authorities")
	public Map<String, Boolean> getAll() throws Exception {
		List<Authority> authorityGetAll = authorityService.getAll();
		Map<String, Boolean> response = new HashMap<>();

		response.put(authorityGetAll + "Yetkilendirilmiş kişiler listelendi.", Boolean.TRUE);
		return response;
	}

	@PostMapping("/authorities")
	public Map<String, Boolean> add(@RequestBody Authority authority) throws Exception {
		Authority authorityAdd = authorityService.add(authority);
		Map<String, Boolean> response = new HashMap<>();
		if (authorityAdd == null) {
			response.put("Kişi yetkilendirilemedi.", Boolean.FALSE);
			return response;
		} else {
			response.put(authorityAdd.getAuthorityName() + " adlı kişi yetkilendirildi.", Boolean.TRUE);
			return response;
		}
	}

	@DeleteMapping("/authorities")
	public Map<String, Boolean> delete(@RequestBody Authority authority) throws Exception {

		Authority authorityDelete = authorityService.findById(authority.getAuthorityId())
				.orElseThrow(() -> new Exception("Bu kimliğe sahip kredi girişi yok : " + authority.getAuthorityId()));

		authorityService.delete(authorityDelete);
		Map<String, Boolean> response = new HashMap<>();
		response.put("Yetkilendirilmiş kişi silindi", Boolean.TRUE);
		return response;
	}

	@PostMapping("/authorities/registration/{id}/{cid}")
	public Map<String, Boolean> completeCustomerInfo(@PathVariable("id") Integer authorityId,
			@PathVariable("cid") Integer customerId, @RequestBody RegistrationRequest request) throws Exception {
		Authority authority = authorityService.findById(authorityId).orElseThrow(
				() -> new Exception("Üzgünüm bu işlem için yetkili kişiler arasında değilsiniz. : " + authorityId));

		Customer customer = customerService.findById(customerId).orElseThrow(
				() -> new Exception("Bu kimliğe sahip kayıt aşamasında müşteri bulunmamaktadır. : " + customerId));

		Map<String, Boolean> response = new HashMap<>();
		if (request.isBlackList() == true) {
			authorityService.completeCustomerInfo(customerId, request);
			customer.setLoanLimit(0);
			response.put(customerId + " ID'li müşteri karalistede olduğu için kredi limiti belirlenemedi.",
					Boolean.FALSE);
			return response;
		} else {
			authorityService.completeCustomerInfo(customerId, request);
			response.put("Sayın " + authority.getAuthorityName()
					+ ", müşteri bilgileri başarıyla tamamladınız. Bilgileri girilen müşterinin kredi limiti hesaplandı.",
					Boolean.TRUE);
			return response;
		}

	}

	@PostMapping("/authorities/registration/update/{id}/{cid}")
	public Map<String, Boolean> updateCustomerBlacklist(@PathVariable("id") Integer authorityId,
			@PathVariable("cid") Integer customerId, @RequestBody RegistrationRequest request) throws Exception {
		Authority authority = authorityService.findById(authorityId).orElseThrow(
				() -> new Exception("Üzgünüm bu işlem için yetkili kişiler arasında değilsiniz. : " + authorityId));

		Customer customer = customerService.findById(customerId).orElseThrow(
				() -> new Exception("Bu kimliğe sahip kayıt aşamasında müşteri bulunmamaktadır. : " + customerId));

		Map<String, Boolean> response = new HashMap<>();

		if (request.isBlackList() == true) {
			authorityService.updateBlacklist(customerId, request);
			customerService.add(customer);
			response.put("Sayın " + authority.getAuthorityName()
					+ ", müşteri bilgileri başarıyla güncellediniz. Karalisteye giren müşterinin kredi limiti güncellendi.",
					Boolean.FALSE);
			return response;
		} else {
			authorityService.updateBlacklist(customerId, request);
			customerService.add(customer);
			response.put("Sayın " + authority.getAuthorityName()
					+ ", müşteri bilgileri başarıyla güncellediniz. Karalisteden çıkan müşterinin kredi limiti güncellendi.",
					Boolean.TRUE);
			return response;
		}

	}

	@PostMapping("/authorities/loanrequests/{id}/{rid}")
	public Map<String, Boolean> confirmRequestLoan(@PathVariable("id") Integer authorityId,
			@PathVariable("rid") Integer requestId) throws Exception {
		Authority authority = authorityService.findById(authorityId).orElseThrow(
				() -> new Exception("Üzgünüm bu işlem için yetkili kişiler arasında değilsiniz. : " + authorityId));

		String feedback = authorityService.confirmRequestLoan(requestId);

		Map<String, Boolean> response = new HashMap<>();
		if (feedback == "reddedildi") {
			response.put(
					"Sayın " + authority.getAuthorityName()
							+ ", belirlediğiniz kredi girişi onaylanmadı. Güncelleme için müşteriyle iletişime geçin.",
					Boolean.FALSE);
			return response;
		} else {
			response.put("Sayın " + authority.getAuthorityName() + ", belirlediğiniz kredi girişi başarıyla onaylandı.",
					Boolean.TRUE);
			return response;
		}

	}

	@GetMapping("/authorities/loanrequests/{id}")
	public Map<String, Boolean> getNullRequestLoan(@PathVariable("id") Integer authorityId) throws Exception {
		Authority authority = authorityService.findById(authorityId).orElseThrow(
				() -> new Exception("Üzgünüm bu işlem için yetkili kişiler arasında değilsiniz. : " + authorityId));

		List<LoanRequest> requestedLoanGetAll = loanRequestService.findByFeedback(null);

		List<String> temporaryList = new ArrayList<>();
		for (int i = 0; i < requestedLoanGetAll.size(); i++) {
			temporaryList.add("[");
			temporaryList.add("Onay bekleyen kredi idsi:  " + String.valueOf(requestedLoanGetAll.get(i).getId()));
			temporaryList.add("Onay bekleyen kredinin müşteri idsi: "
					+ String.valueOf(requestedLoanGetAll.get(i).getCustomerId()));
			temporaryList
					.add("Onay bekleyen kredinin tutarı: " + String.valueOf(requestedLoanGetAll.get(i).getAmount()));
			temporaryList.add("]");
		}

		Map<String, Boolean> response = new HashMap<>();
		response.put(temporaryList + " Sayın," + authority.getAuthorityName() + " " + authority.getAuthoritySurname()
				+ ", onay bekleyen tüm kredi girişleri listelendi.", Boolean.TRUE);
		return response;
	} 

}
