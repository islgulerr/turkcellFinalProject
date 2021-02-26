package com.example.loanModuleDemo.entities.concretes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.loanModuleDemo.entities.abstracts.IEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "loan_requests")
public class LoanRequest implements IEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	private int id;
	@Column(name = "customer_id")
	private int customerId;
	@Column(name = "loan_document")
	private String loanDocument;
	@Column(name = "amount")
	private double amount;
	@Column(name = "feedback")
	private String feedback;

}
