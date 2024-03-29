package com.itgate.springjwt.controllers;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itgate.springjwt.models.Client;
import com.itgate.springjwt.models.Commentaire;
import com.itgate.springjwt.models.Hotel;
import com.itgate.springjwt.models.ResourceNotFoundException;
import com.itgate.springjwt.models.Vehicule;
import com.itgate.springjwt.models.Vol;
import com.itgate.springjwt.repository.CommentaireRepository;
import com.itgate.springjwt.repository.HotelRepository;
import com.itgate.springjwt.repository.VehiculeRepository;
import com.itgate.springjwt.utils.StorageService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/Commentairs")
@CrossOrigin("*")
public class CommentaireController {
	@Autowired
	HotelRepository hotelRepository;
	@Autowired
	VehiculeRepository vehiculeRepository;
	@Autowired
	CommentaireRepository commentaireRepository;
	 @Autowired
		private StorageService service;
	
	 @GetMapping("/{hotel_id}")
	    public Collection<Commentaire> getCommentsByHotelId(@PathVariable Long hotel_id) {
	        return commentaireRepository.findByHotelId(hotel_id);
	    }
	

	 @PostMapping("/hotels/{hotel_id}/comments/{client_id}")
	 public Commentaire createComment(@PathVariable(value = "hotel_id") Hotel hotel_id,
	                                   @PathVariable(value = "client_id") Client client_id,
	                                   @Valid @RequestBody Commentaire commentaire,
	                                   BindingResult result) {

	     if (result.hasErrors()) {
	         for (ObjectError error : result.getAllErrors()) {
	             System.out.println(error.getDefaultMessage());
	         } 
	         throw new IllegalArgumentException("Invalid input for Commentaire");
	     }

	     // Set the hotel and client properties based on the path variables
	     commentaire.setHotel(hotel_id);
	     commentaire.setClient(client_id);

	     // Set the contenu and dateCreation properties based on the request body
	     String contenu = commentaire.getContenu();
	     if (contenu == null || contenu.trim().isEmpty()) {
	         result.rejectValue("contenu", "NotNull", "Contenu cannot be null or empty");
	         throw new IllegalArgumentException("Invalid input for Commentaire");
	     }
	     commentaire.setDateCreation(LocalDateTime.now());

	     return commentaireRepository.save(commentaire);
	 }
	 @GetMapping("/vehicules/{vehicule_id}")
	    public Collection<Commentaire> getCommentsByVehiculeId2(@PathVariable Long vehicule_id) {
	        return commentaireRepository.findByVehiculeId(vehicule_id);
	    }
	

	 @PostMapping("/vehiculs/{vehicule_id}/comments/{client_id}")
	 public Commentaire createComment2(@PathVariable(value = "vehicule_id") Vehicule vehicule_id,
	                                   @PathVariable(value = "client_id") Client client_id,
	                                   @Valid @RequestBody Commentaire commentaire,
	                                   BindingResult result) {

	     if (result.hasErrors()) {
	         for (ObjectError error : result.getAllErrors()) {
	             System.out.println(error.getDefaultMessage());
	         }
	         throw new IllegalArgumentException("Invalid input for Commentaire");
	     }

	     // Set the hotel and client properties based on the path variables
	     commentaire.setVehicule(vehicule_id);
	     commentaire.setClient(client_id);

	     // Set the contenu and dateCreation properties based on the request body
	     String contenu = commentaire.getContenu();
	     if (contenu == null || contenu.trim().isEmpty()) {
	         result.rejectValue("contenu", "NotNull", "Contenu cannot be null or empty");
	         throw new IllegalArgumentException("Invalid input for Commentaire");
	     }
	     commentaire.setDateCreation(LocalDateTime.now());

	     return commentaireRepository.save(commentaire);
	 }
	 @PutMapping("/vehiculs/{vehicule_id}/comments/{client_id}/{id}")
	 public Commentaire updateComment(@PathVariable(value = "vehicule_id") Vehicule vehicule_id,
	                                   @PathVariable(value = "client_id") Client client_id,
	                                   @PathVariable(value = "id") Long id,
	                                   @Valid @RequestBody Commentaire commentaire,
	                                   BindingResult result) {
	     if (result.hasErrors()) {
	         for (ObjectError error : result.getAllErrors()) {
	             System.out.println(error.getDefaultMessage());
	         }
	         throw new IllegalArgumentException("Invalid input for Commentaire");
	     }

	     // Set the vehicule and client properties based on the path variables
	     commentaire.setVehicule(vehicule_id);
	     commentaire.setClient(client_id);

	     // Set the dateCreation property based on the request body
	     commentaire.setDateCreation(LocalDateTime.now());

	     // Update an existing comment
	     Optional<Commentaire> optionalCommentaire = commentaireRepository.findById(id);
	     if (optionalCommentaire.isPresent()) {
	         Commentaire existingCommentaire = optionalCommentaire.get();
	         if (!existingCommentaire.getClient().equals(client_id)) {
	             throw new IllegalArgumentException("You are not authorized to update this comment");
	         }
	         existingCommentaire.setContenu(commentaire.getContenu());
	         return commentaireRepository.save(existingCommentaire);
	     } else {
	         throw new IllegalArgumentException("Commentaire not found with id " + id);
	     }
	 }
	 @PutMapping("/hotel/{hotel_id}/comments/{client_id}/{id}")
	 public Commentaire updateComment1(@PathVariable(value = "hotel_id") Hotel hotel_id,
	                                   @PathVariable(value = "client_id") Client client_id,
	                                   @PathVariable(value = "id") Long id,
	                                   @Valid @RequestBody Commentaire commentaire,
	                                   BindingResult result) {
	     if (result.hasErrors()) {
	         for (ObjectError error : result.getAllErrors()) {
	             System.out.println(error.getDefaultMessage());
	         }
	         throw new IllegalArgumentException("Invalid input for Commentaire");
	     }

	     // Set the hotel and client properties based on the path variables
	     commentaire.setHotel(hotel_id);
	     commentaire.setClient(client_id);

	     // Set the dateCreation property based on the request body
	     commentaire.setDateCreation(LocalDateTime.now());

	     // Update an existing comment
	     Optional<Commentaire> optionalCommentaire = commentaireRepository.findById(id);
	     if (optionalCommentaire.isPresent()) {
	         Commentaire existingCommentaire = optionalCommentaire.get();
	         if (!existingCommentaire.getClient().equals(client_id)) {
	             throw new IllegalArgumentException("You are not authorized to update this comment");
	         }
	         existingCommentaire.setContenu(commentaire.getContenu());
	         return commentaireRepository.save(existingCommentaire);
	     } else {
	         throw new IllegalArgumentException("Commentaire not found with id " + id);
	     }
	 }
		@DeleteMapping("/Delet/{id}")
		public ResponseEntity DeletCommentaire(@PathVariable Long id) {
			commentaireRepository.deleteById(id);
			 return ResponseEntity.ok().build();
		}
		@GetMapping("/files/{filename:.+}")
		public ResponseEntity<Resource>getFile(@PathVariable String filename){
		        Resource file=service.loadFile(filename);
		        return ResponseEntity.ok()	
		                .header(HttpHeaders.CONTENT_DISPOSITION,"attachement;filename=\""+file.getFilename()+"\"")
		                        .body(file);
		}
}






