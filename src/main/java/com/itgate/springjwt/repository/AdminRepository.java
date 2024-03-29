package com.itgate.springjwt.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itgate.springjwt.models.Admin;


public interface AdminRepository extends JpaRepository<Admin, Long>{
	Admin findByEmail(String email);

}
