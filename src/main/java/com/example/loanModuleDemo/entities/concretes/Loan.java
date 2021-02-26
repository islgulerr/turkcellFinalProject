package com.example.loanModuleDemo.entities.concretes;

import java.util.Date;

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
@Table(name = "loans")
public class Loan implements IEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loan_id")
	private int id;
	@Column(name = "customer_id")
	private int customerId;
	@Column(name = "amount")
	private double amount;
	@Column(name = "drawdown_date")
	private Date drawdownDate;
	@Column(name = "loan_document")
	private String loanDocument;
}
