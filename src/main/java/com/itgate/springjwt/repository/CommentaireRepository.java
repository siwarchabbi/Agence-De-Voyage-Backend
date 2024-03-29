package com.itgate.springjwt.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itgate.springjwt.models.Commentaire;
import com.itgate.springjwt.models.Hotel;


@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long>{

	
	
	Collection<Commentaire> findByHotelId(Long hotel_id);

	Collection<Commentaire> findByVehiculeId(Long vehicule_id);


}
