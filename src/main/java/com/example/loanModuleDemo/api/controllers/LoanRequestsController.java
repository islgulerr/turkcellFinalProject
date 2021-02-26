package com.example.loanModuleDemo.api.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.loanModuleDemo.LoanRequestPDFExporter;
import com.example.loanModuleDemo.business.abstracts.ILoanRequestService;
import com.example.loanModuleDemo.entities.concretes.LoanRequest;
import com.lowagie.text.DocumentException;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class LoanRequestsController {

	@Autowired
	private ILoanRequestService loanRequestService;

	@PostMapping("/loanrequests")
	public Map<String, Boolean> add(@RequestBody LoanRequest loanRequest) throws Exception {

		LoanRequest loanRequestAdd = loanRequestService.add(loanRequest);
		Map<String, Boolean> response = new HashMap<>();
		if (loanRequestAdd == null) {
			response.put("Kredi girişi bilgileri eklenemedi.", Boolean.FALSE);
			return response;
		} else {
			response.put(loanRequestAdd + "Kredi girişi bilgileri eklendi.", Boolean.TRUE);
			return response;
		}
	}

	@DeleteMapping("/loanrequests")
	public Map<String, Boolean> delete(@RequestBody LoanRequest loanRequest) throws Exception {

		LoanRequest loanRequestDelete = loanRequestService.findById(loanRequest.getId())
				.orElseThrow(() -> new Exception("Bu kimliğe sahip kredi girişi yok : " + loanRequest.getId()));

		loanRequestService.delete(loanRequestDelete);
		Map<String, Boolean> response = new HashMap<>();
		response.put("Kredi girişi silindi", Boolean.TRUE);
		return response;
	}

	@GetMapping("/loanrequest/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {

		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=loanrequests_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		List<LoanRequest> listLoanRequests = loanRequestService.getAll();

		LoanRequestPDFExporter exporter = new LoanRequestPDFExporter(listLoanRequests);
		exporter.export(response);

	}
}
