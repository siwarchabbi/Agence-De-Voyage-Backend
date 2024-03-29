package com.itgate.springjwt.models;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Admin extends User {
	
	private String Description;
	 private String image ;
	 @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
	    private Collection<Reclamation> reclamations = new ArrayList<>();
	 
	 @OneToMany(mappedBy = "admin",cascade = CascadeType.REMOVE)
	    private Collection<Hotel> hotels;
	    @OneToMany(mappedBy = "admin",cascade = CascadeType.REMOVE)
	    private Collection<Vehicule> vehicules;
	    @OneToMany(mappedBy = "admin",cascade = CascadeType.REMOVE)
	    private Collection<Vol> vols;
	 
	 public Admin(String username, String email, String password, String description) {
	        super(username, email, password);
	        this.Description = description;
	       
	    }
	    public Admin(){}
	 
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	@JsonIgnore
	public Collection<Reclamation> getReclamations() {
		return reclamations;
	}
	public void setReclamations(Collection<Reclamation> reclamations) {
		this.reclamations = reclamations;
	}
	@JsonIgnore
	public Collection<Hotel> getHotels() {
		return hotels;
	}
	public void setHotels(Collection<Hotel> hotels) {
		this.hotels = hotels;
	}
	@JsonIgnore
	public Collection<Vehicule> getVehicules() {
		return vehicules;
	}
	public void setVehicules(Collection<Vehicule> vehicules) {
		this.vehicules = vehicules;
	}
	@JsonIgnore
	public Collection<Vol> getVols() {
		return vols;
	}
	public void setVols(Collection<Vol> vols) {
		this.vols = vols;
	}
	
	
	
	
	

}
