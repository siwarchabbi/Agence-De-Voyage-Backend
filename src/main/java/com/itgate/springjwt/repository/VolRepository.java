package com.itgate.springjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itgate.springjwt.models.Vol;


@Repository
public interface VolRepository extends JpaRepository<Vol, Long> {

}
