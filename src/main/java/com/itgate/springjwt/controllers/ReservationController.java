package com.itgate.springjwt.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itgate.springjwt.models.Reservation;
import com.itgate.springjwt.models.Vehicule;
import com.itgate.springjwt.models.Vol;
import com.itgate.springjwt.repository.ClientRepository;
import com.itgate.springjwt.repository.HotelRepository;
import com.itgate.springjwt.repository.ReservationRepository;
import com.itgate.springjwt.repository.VehiculeRepository;
import com.itgate.springjwt.repository.VolRepository;
import com.itgate.springjwt.models.Client;
import com.itgate.springjwt.models.Hotel;


@RestController
@RequestMapping("/Reservation")
@CrossOrigin("*")

public class ReservationController {

	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	ClientRepository clientRepository;
	@Autowired
     HotelRepository hotelRepository;
	@Autowired
	VolRepository volRepository;
	@Autowired
	VehiculeRepository vehiculeRepository;
	 @GetMapping
	    public List<Reservation> getall() {
	        return reservationRepository.findAll();
	    }
	 @GetMapping("/reservations/hotels")
	 public Collection<Reservation> getReservationHotels() {
	     return reservationRepository.findByHotelsIsNotNull();
	 }
	 @GetMapping("/reservations/{reservation_id}/hotels/{hotel_id}")
	 public ResponseEntity<Hotel> getHotelByIdFromReservation(@PathVariable Long reservation_id, @PathVariable Long hotel_id) {
	     Reservation reservation = reservationRepository.findById(reservation_id).orElse(null);
	     if(reservation != null) {
	         Optional<Hotel> hotel = reservation.getHotels().stream()
	                     .filter(h -> h.getId().equals(hotel_id))
	                     .findFirst();
	         if(hotel.isPresent()) {
	             return ResponseEntity.ok(hotel.get());
	         }
	     }
	     return ResponseEntity.notFound().build();
	 }

	    @PostMapping("/{client_id}")
	    public Reservation saveall(@RequestBody Reservation r, @PathVariable Long client_id) {
	        Client c = clientRepository.findById(client_id).orElse(null);
	        r.setClient(c);
	        return reservationRepository.save(r);
	    }

	    @GetMapping("/{id}")
	    public Reservation getOneOrder(@PathVariable Long id) {
	        return reservationRepository.findById(id).orElse(null);
	    }

	    @PutMapping("/{id}")
	    public Reservation updateOrder(@RequestBody Reservation r, @PathVariable Long id) {
	        Reservation r1 = reservationRepository.findById(id).orElse(null);
	        if (r1 != null) {
	            r.setId(id);
	            r.setClient(r.getClient());
	            return reservationRepository.saveAndFlush(r);
	        } else {
	            throw new RuntimeException("fail");
	        }
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity deleteOrder(@PathVariable Long id) {
	        reservationRepository.deleteById(id);
	        return ResponseEntity.ok().build();

	    }
	    
	    
	 /*  @PostMapping("/savehotel/{client_id}")
	    public Reservation savehotel(@RequestBody Reservation r,@RequestParam List<Long> ids,@PathVariable Long client_id ){

	        Client c=clientRepository.findById(client_id).orElse(null);
	        r.setClient(c);
	        for(int i =0;i<ids.size();i++){

	            Hotel r1=hotelRepository.findById(ids.get(i)).orElse(null);
	            r.addHotel(r1);
	            System.out.println(r);
	        }
	        reservationRepository.save(r);
	        return  reservationRepository.save(r);
	    }*/
	    @PostMapping("/savehotel2/{client_id}")
	    public ResponseEntity<?> savehotels(@RequestBody Reservation r, 
	                                        @RequestParam List<Long> hotel_id, 
	                                        @PathVariable Long client_id) {

	        Client c = clientRepository.findById(client_id).orElse(null);
	        if(c == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
	        }
	        r.setClient(c);

	        List<Hotel> hotels = new ArrayList<>();
	        for(int i = 0; i < hotel_id.size(); i++){
	            Hotel h = hotelRepository.findById(hotel_id.get(i)).orElse(null);
	            if(h == null) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel with id " + hotel_id.get(i) + " not found");
	            }
	            hotels.add(h);
	        }
	        r.setHotels(hotels);
	        r.setStatus("processing");
	        Reservation savedReservation = reservationRepository.save(r);
	        return ResponseEntity.ok(savedReservation);
	    }
	    
	    
	    @PutMapping("/reservations/{reservationId}/status")
	    public ResponseEntity<?> updateReservationStatus(@PathVariable Long reservationId, @RequestParam String newStatus) {
	        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
	        if (reservation == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation with id " + reservationId + " not found");
	        }
	        if (!Arrays.asList("processing", "success", "not_accessible").contains(newStatus)) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid status: " + newStatus);
	        }
	        reservation.setStatus(newStatus);
	        Reservation savedReservation = reservationRepository.save(reservation);
	        return ResponseEntity.ok(savedReservation);
	    }

	    
	    @PostMapping("/savevehicule/{client_id}")
	    public ResponseEntity<?> savevehiculss(@RequestBody Reservation r,
	                                            @RequestParam List<Long> vehicule_id,
	                                            @PathVariable Long client_id) {

	        Client c = clientRepository.findById(client_id).orElse(null);
	        if(c == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
	        }
	        r.setClient(c);

	        List<Vehicule> vehicules = new ArrayList<>();
	        for(int i = 0; i < vehicule_id.size(); i++){
	            Vehicule h = vehiculeRepository.findById(vehicule_id.get(i)).orElse(null);
	            if(h == null) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicule with id " + vehicule_id.get(i) + " not found");
	            }
	            vehicules.add(h);
	        }
	        r.setVehicules(vehicules);

	        // Check for overlapping reservations
	        Collection<Reservation> overlappingReservations = reservationRepository.findByVehiculesAndDateRange(vehicules, r.getDate_debutr(), r.getDate_finr());
	        if (!overlappingReservations.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Selected date range is not available.");
	        }

	        Reservation savedReservation = reservationRepository.save(r);
	        return ResponseEntity.ok(savedReservation);
	    }

	    @PostMapping("/savevoles2/{client_id}")
	    public ResponseEntity<?> savevoles(@RequestBody Reservation r, 
	                                        @RequestParam List<Long> vol_id, 
	                                        @PathVariable Long client_id) {

	        Client c = clientRepository.findById(client_id).orElse(null);
	        if(c == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
	        }
	        r.setClient(c);

	        Collection<Vol> vols = new ArrayList<>();
	        for(int i = 0; i < vol_id.size(); i++){
	            Vol h = volRepository.findById(vol_id.get(i)).orElse(null);
	            if(h == null) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("	Vol with id " + vol_id.get(i) + " not found");
	            }
	            vols.add(h);
	        }
	        r.setVols(vols);
	        r.setStatus("processing");
	        Reservation savedReservation = reservationRepository.save(r);
	        return ResponseEntity.ok(savedReservation);
	    }


	    
	  

	    
	    

	    @GetMapping("/reservations/{clientId}")
	    public Collection<Reservation> getReservationsByClientId(@PathVariable Long clientId) {
	        return reservationRepository.findByClientId(clientId);
	    }


}
