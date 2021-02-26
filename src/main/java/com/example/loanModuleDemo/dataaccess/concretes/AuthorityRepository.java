package com.example.loanModuleDemo.dataaccess.concretes;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.loanModuleDemo.entities.concretes.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

}
