package com.itgate.springjwt.controllers;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itgate.springjwt.models.Client;
import com.itgate.springjwt.models.Favorie;
import com.itgate.springjwt.models.Hotel;
import com.itgate.springjwt.models.ResourceNotFoundException;
import com.itgate.springjwt.models.Vehicule;
import com.itgate.springjwt.repository.ClientRepository;
import com.itgate.springjwt.repository.FavorieRepository;
import com.itgate.springjwt.repository.HotelRepository;
import com.itgate.springjwt.repository.VehiculeRepository;

@RestController
@RequestMapping("/favorites")
@CrossOrigin("*")
public class FavorieController {
	
	 @Autowired
	    private FavorieRepository favorieRepository;
	 @Autowired
	 private HotelRepository hotelRepository;
	 @Autowired
	 private VehiculeRepository vehiculeRepository;
	 @Autowired
	 private ClientRepository clientRepository;
	 
	 @GetMapping("/client")
	 public ResponseEntity<Collection<Favorie>> getFavoritesByClientId(@RequestParam("client_id") Long clientId) {
	     Collection<Favorie> favorites = favorieRepository.getFavoritesByClientId(clientId);
	     return new ResponseEntity<>(favorites, HttpStatus.OK);
	 }
	 @GetMapping("/client2")
	 public ResponseEntity<Collection<Favorie>> getFavoritesByClientId2(@RequestParam("client_id") Long clientId) {
	     Collection<Favorie> favorites = favorieRepository.getFavoritesByClientId2(clientId);
	     return new ResponseEntity<>(favorites, HttpStatus.OK);
	 }




	    

	    @PostMapping("/addfavorie")
	    public Favorie addFavorite( @RequestParam("client_id") Long client_id, @RequestParam("hotel_id") Long hotel_id) {
	      
	        Client client = clientRepository.findById(client_id)
	                                         .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + client_id));
	      
	        Hotel hotel = hotelRepository.findById(hotel_id)
	                                      .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id " + hotel_id));
	        Favorie favorie = new Favorie();
	        favorie.setClient(client);
	        favorie.setHotel(hotel);
	        favorie.setDate(new Date());
	        return favorieRepository.save(favorie);
	    }

	    @PostMapping("/addfavoriev")
	    public Favorie addFavoritev( @RequestParam("client_id") Long client_id, @RequestParam("vehicule_id") Long vehicule_id) {
	      
	        Client client = clientRepository.findById(client_id)
	                                         .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + client_id));
	      
	        Vehicule vehicule = vehiculeRepository.findById(vehicule_id)
	                                      .orElseThrow(() -> new ResourceNotFoundException("vehicule not found with id " + vehicule_id));
	        Favorie favorie = new Favorie();
	        favorie.setClient(client);
	        favorie.setVehicule(vehicule);
	        favorie.setDate(new Date());
	        return favorieRepository.save(favorie);
	    }

	    @DeleteMapping("/{id}")
	    public void deleteFavorite(@PathVariable Long id) {
	        favorieRepository.deleteById(id);
	    }

}
