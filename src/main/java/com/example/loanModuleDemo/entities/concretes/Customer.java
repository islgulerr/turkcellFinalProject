package com.example.loanModuleDemo.entities.concretes;

import java.sql.Date;

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
@Table(name = "customers")
public class Customer implements IEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private int id;
	@Column(name = "identity_no")
	private String identityNo;
	@Column(name = "customer_name")
	private String customerName;
	@Column(name = "customer_surname")
	private String customerSurname;
	@Column(name = "gsm_no")
	private String gsmNo;
	@Column(name = "address")
	private String address;
	@Column(name = "birth_date")
	private Date birthDate;
	@Column(name = "customer_type")
	private String customerType;
	@Column(name = "subs_date")
	private Date subsDate;
	@Column(name = "loan_limit")
	private double loanLimit;
	@Column(name = "blacklist")
	private boolean blackList;
	@Column(name = "identity_document")
	private String identityDocument;

}
