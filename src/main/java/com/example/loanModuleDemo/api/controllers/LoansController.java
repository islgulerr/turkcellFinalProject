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

import com.example.loanModuleDemo.LoanPDFExporter;
import com.example.loanModuleDemo.business.abstracts.ILoanService;
import com.example.loanModuleDemo.entities.concretes.Loan;
import com.lowagie.text.DocumentException;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class LoansController {

	@Autowired   
	private ILoanService loanService;

	@PostMapping("/loans")
	public Map<String, Boolean> add(@RequestBody Loan loan) throws Exception {
		Loan loanAdd = loanService.add(loan);
		Map<String, Boolean> response = new HashMap<>();
		if (loanAdd == null) {
			response.put("Kredi bilgileri eklenemedi.", Boolean.FALSE);
			return response;
		} else {
			response.put(loanAdd + "Kredi bilgileri eklendi.", Boolean.TRUE);
			return response;
		}
	}

	@DeleteMapping("/loans")
	public Map<String, Boolean> delete(@RequestBody Loan loan) throws Exception {

		Loan loanDelete = loanService.findById(loan.getId())
				.orElseThrow(() -> new Exception("Bu kimliğe sahip kredi girişi yok : " + loan.getId()));

		loanService.delete(loanDelete);
		Map<String, Boolean> response = new HashMap<>();
		response.put("Kredi silindi", Boolean.TRUE);
		return response;
	}

	@GetMapping("/loans/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=loans_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		List<Loan> listLoans = loanService.getAll();

		LoanPDFExporter exporter = new LoanPDFExporter(listLoans);
		exporter.export(response);

	}

}
