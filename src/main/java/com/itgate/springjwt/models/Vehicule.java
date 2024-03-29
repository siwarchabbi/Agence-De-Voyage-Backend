package com.itgate.springjwt.models;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
@Entity
public class Vehicule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String matricule;
	private String chauffeur;
	private String pays;
	private String place;
	private String name;
	private String prix;
    private int prixpromo;
    private Boolean status ; 
	private Boolean promo;
	private ArrayList<String> images=new ArrayList<>();
	 private String image;
	@ManyToOne
	   @JoinColumn(name = "admin_id")
	   private Admin admin;
	
	 @OneToMany(mappedBy = "vehicule")
	 private Collection<Commentaire> commentaires;
	 
	 @ManyToMany(mappedBy = "vehicules")
	    private Collection<Reservation> reservations;
	 
	 @OneToMany( mappedBy = "vehicule" , cascade = CascadeType.REMOVE)
	   private Collection<Favorie> favories;
	 
	
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrix() {
		return prix;
	}
	public void setPrix(String prix) {
		this.prix = prix;
	}
	public ArrayList<String> getImages() {
		return images;
	}
	public void setImages(ArrayList<String> images) {
		this.images = images;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getPays() {
		return pays;
	}
	public void setPays(String pays) {
		this.pays = pays;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getMatricule() {
		return matricule;
	}
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	public String getChauffeur() {
		return chauffeur;
	}
	public void setChauffeur(String chauffeur) {
		this.chauffeur = chauffeur;
	}
	
	
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	public Collection<Reservation> getReservations() {
		return reservations;
	}
	public void setReservations(Collection<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	public int getPrixpromo() {
		return prixpromo;
	}
	public void setPrixpromo(int prixpromo) {
		this.prixpromo = prixpromo;
	}
	public Boolean getPromo() {
		return promo;
	}
	public void setPromo(Boolean promo) {
		this.promo = promo;
	}
	public void addReservation(Reservation r) {
		 if(reservations==null){
		        reservations=new ArrayList<>();
		    }
		    reservations.add(r);
		}
	@JsonIgnore
	public Collection<Commentaire> getCommentaires() {
		return commentaires;
	}
	public void setCommentaires(Collection<Commentaire> commentaires) {
		this.commentaires = commentaires;
	}
	
	@JsonIgnore
	public Collection<Favorie> getFavories() {
		return favories;
	}
	public void setFavories(Collection<Favorie> favories) {
		this.favories = favories;
	}
	
	
	

}
