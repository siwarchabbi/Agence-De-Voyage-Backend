package com.itgate.springjwt.repository;

import com.itgate.springjwt.models.Client;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client,Long> {

    Client findByEmail(String email);
    
    @Transactional
    @Modifying
    @Query("UPDATE Client u SET u.confirm = CASE u.confirm WHEN true THEN false ELSE true END WHERE u.id = :id")
    void toggleClientConfirm(@Param("id") Long id);
   
}
