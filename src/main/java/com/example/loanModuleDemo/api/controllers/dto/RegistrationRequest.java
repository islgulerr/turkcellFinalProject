package com.example.loanModuleDemo.api.controllers.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class RegistrationRequest {

	private String gsmNo;
	private String address;
	private Date birthDate;
	private String identityNo;
	private String customerName;
	private String customerType;
	private String customerSurname;

	private String identityDocument;

	private Date subsDate;
	private double loanLimit;
	private boolean blackList;

	private double amount;
	private String loanDocument;

	private String feedback;

}
