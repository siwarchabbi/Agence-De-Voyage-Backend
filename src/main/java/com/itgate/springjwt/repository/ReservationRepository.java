package com.itgate.springjwt.repository;

import java.sql.Date;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itgate.springjwt.models.Reservation;
import com.itgate.springjwt.models.Vehicule;

import jakarta.validation.constraints.AssertFalse.List;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	Collection<Reservation> findByClientId(Long clientId);
	Collection <Reservation> findByHotelsIsNotNull();
	 @Query("SELECT r FROM Reservation r JOIN r.vehicules v WHERE v IN :vehicules AND ((r.date_debutr <= :date_finr) AND (r.date_finr >= :date_debutr))")
	    Collection<Reservation> findByVehiculesAndDateRange(@Param("vehicules") Collection<Vehicule> vehicules, @Param("date_debutr") java.util.Date date, @Param("date_finr") java.util.Date date2);
}
