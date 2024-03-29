package com.itgate.springjwt.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "favorites")
public class Favorie {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	  @Temporal(TemporalType.TIMESTAMP)
	    private Date date;
	    
	  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "client_id")
	    private Client client;
	    

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "hotel_id")
	    private Hotel hotel;

	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "vehicule_id")
	    private Vehicule vehicule;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Client getClient() {
			return client;
		}

		public void setClient(Client client) {
			this.client = client;
		}

		public Hotel getHotel() {
			return hotel;
		}

		public void setHotel(Hotel hotel) {
			this.hotel = hotel;
		}
		public Vehicule getVehicule() {
			return vehicule;
		}

		public void setVehicule(Vehicule vehicule) {
			this.vehicule = vehicule;
		}
		
		public Favorie(Long id, Date date, Client client, Hotel hotel, Vehicule vehicule) {
		    this.id = id;
		    this.date = date;
		    this.client = client;
		    this.hotel = hotel;
		    this.vehicule = vehicule;
		}


		public Favorie() {
			
			
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

	
	    
	    
	    
	    
}
