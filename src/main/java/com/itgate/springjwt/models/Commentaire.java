package com.itgate.springjwt.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "commentaires")
public class Commentaire {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	 
	    @ManyToOne
	    @JoinColumn(name = "hotel_id")
	    private Hotel hotel;
	    @ManyToOne
	    @JoinColumn(name = "vehicule_id")
	    private Vehicule vehicule;
	    @ManyToOne
	    @JoinColumn(name = "client_id")
	    private Client client;
	 
	    private String contenu;
	 
	    private LocalDateTime dateCreation;
	    
	    public Commentaire(String contenu, LocalDateTime dateCreation) {
	       
	        this.contenu = contenu;
	        this.dateCreation = dateCreation;
	    }
	    public Commentaire(){}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Hotel getHotel() {
			return hotel;
		}

		public void setHotel(Hotel hotel) {
			this.hotel = hotel;
		}

		public Client getClient() {
			return client;
		}

		public void setClient(Client client) {
			this.client = client;
		}

		public String getContenu() {
			return contenu;
		}

		public void setContenu(String contenu) {
			this.contenu = contenu;
		}

		public LocalDateTime getDateCreation() {
			return dateCreation;
		}

		public void setDateCreation(LocalDateTime dateCreation) {
			this.dateCreation = dateCreation;
		}
		public Vehicule getVehicule() {
			return vehicule;
		}
		public void setVehicule(Vehicule vehicule) {
			this.vehicule = vehicule;
		}
	    
	    
	    
	    
}
