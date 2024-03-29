package com.itgate.springjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itgate.springjwt.models.Hotel;


@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>{

	Hotel getHotelById(Long hotelId);

}
