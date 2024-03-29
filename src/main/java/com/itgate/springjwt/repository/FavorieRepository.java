package com.itgate.springjwt.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itgate.springjwt.models.Favorie;

@Repository
public interface FavorieRepository  extends JpaRepository<Favorie, Long>{
	
	 
	 @Query("SELECT f FROM Favorie f  JOIN FETCH f.vehicule v WHERE f.client.id = :clientId")
	 Collection<Favorie> getFavoritesByClientId(@Param("clientId") Long clientId);
	 @Query("SELECT f FROM Favorie f   JOIN FETCH f.hotel h  WHERE f.client.id = :clientId")
	 Collection<Favorie> getFavoritesByClientId2(@Param("clientId") Long clientId);
	
	 Collection<Favorie> findByVehiculeId(Long vehicule_id);
	    Collection<Favorie> findByHotelId(Long hotel_id);

}
