package com.itgate.springjwt.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itgate.springjwt.models.Client;
import com.itgate.springjwt.models.Reclamation;

public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
    Collection<Reclamation> findByClient(Client client);
}