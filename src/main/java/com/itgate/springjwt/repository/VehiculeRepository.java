package com.itgate.springjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itgate.springjwt.models.Vehicule;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long>{

}
