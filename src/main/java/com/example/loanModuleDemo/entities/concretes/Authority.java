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
@Table(name = "authorities")
public class Authority implements IEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "authority_id")
	private int authorityId;
	@Column(name = "authority_name")
	private String authorityName;
	@Column(name = "authority_surname")
	private String authoritySurname;
}
