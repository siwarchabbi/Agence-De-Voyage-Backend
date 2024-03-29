package com.itgate.springjwt.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Hotel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private int etoil;
	private String adresse;
	private String chambre;
	private String regien;
	private String pays;
	private String place;
	private int prix;
	private int jours;
	private int prixpromo;
	private Boolean status ; 
	
	private Boolean promo;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date date_debut;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date date_fin;

	private ArrayList<String> images=new ArrayList<>();
	 private String image;
	 @OneToMany(mappedBy = "hotel")
	 private Collection<Commentaire> commentaires;

	@ManyToOne
	   @JoinColumn(name = "admin_id")
	   private Admin admin;
	@OneToMany( mappedBy = "hotel" , cascade = CascadeType.REMOVE)
	   private Collection<Favorie> favories;
	
	 @ManyToMany(mappedBy = "hotels")
	    private Collection<Reservation> reservations;
	 
	 
	 
		
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Boolean getPromo(){
		return promo;
	}
	public void setPromo(Boolean promo) {
		this.promo = promo;
	}
	public Date getDate_debut() {
		return date_debut;
	}
	public void setDate_debut(Date date_debut) {
		this.date_debut = date_debut;
	}
	public Date getDate_fin() {
		return date_fin;
	}
	public void setDate_fin(Date date_fin) {
		this.date_fin = date_fin;
	}
	public int getPrix() {
		return prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}
	public int getJours() {
		return jours;
	}
	public void setJours(int jours) {
		this.jours = jours;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
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

	 
	 
	
	@JsonIgnore
	public Collection<Commentaire> getCommentaires() {
		return commentaires;
	}
	public void setCommentaires(Collection<Commentaire> commentaires) {
		this.commentaires = commentaires;
	}
	public int getEtoil() {
		return etoil;
	}
	public void setEtoil(int etoil) {
		this.etoil = etoil;
	}
	public String getChambre() {
		return chambre;
	}
	public void setChambre(String chambre) {
		this.chambre = chambre;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	


	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	@JsonIgnore
	public Collection<Reservation> getReservations() {
		return reservations;
	}
	public void setReservations(Collection<Reservation> reservations) {
		this.reservations = reservations;
	}

	public void addReservation(Reservation r) {
		 if(reservations==null){
		        reservations=new ArrayList<>();
		    }
		    reservations.add(r);
		}
	public String getRegien() {
		return regien;
	}
	public void setRegien(String regien) {
		this.regien = regien;
	}
	public int getPrixpromo() {
		return prixpromo;
	}
	public void setPrixpromo(int prixpromo) {
		this.prixpromo = prixpromo;
	}
	@JsonIgnore
	public Collection<Favorie> getFavories() {
		return favories;
	}
	public void setFavories(Collection<Favorie> favories) {
		this.favories = favories;
	}

	
	
	
	
}
