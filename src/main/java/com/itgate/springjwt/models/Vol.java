package com.itgate.springjwt.models;

import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

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
public class Vol {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date Date_Depart;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	
	
	
	private Date Date_Arrive;
	
	
	
	@DateTimeFormat(pattern = "HH:mm:ss")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
	private LocalTime heureDepartall;

	@DateTimeFormat(pattern = "HH:mm:ss")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
	private LocalTime heureArriveeall;
	@DateTimeFormat(pattern = "HH:mm:ss")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
	private LocalTime heureDepartarr;

	@DateTimeFormat(pattern = "HH:mm:ss")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
	private LocalTime heureArriveearr;


	private String Aller;
	private String retour;
	 private String image;
	 private int prix;
	 private int prixpromo;
	 private boolean promo;
	 private String status;
	 @ManyToMany(mappedBy = "vols")
	    private Collection<Reservation> reservations;
	
	@ManyToOne
	   @JoinColumn(name = "admin_id")
	   private Admin admin;
	
	
	
	
	
	 
	public LocalTime getHeureDepartall() {
		return heureDepartall;
	}
	public void setHeureDepartall(LocalTime heureDepartall) {
		this.heureDepartall = heureDepartall;
	}
	public LocalTime getHeureArriveeall() {
		return heureArriveeall;
	}
	public void setHeureArriveeall(LocalTime heureArriveeall) {
		this.heureArriveeall = heureArriveeall;
	}
	public LocalTime getHeureDepartarr() {
		return heureDepartarr;
	}
	public void setHeureDepartarr(LocalTime heureDepartarr) {
		this.heureDepartarr = heureDepartarr;
	}
	public LocalTime getHeureArriveearr() {
		return heureArriveearr;
	}
	public void setHeureArriveearr(LocalTime heureArriveearr) {
		this.heureArriveearr = heureArriveearr;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	public Date getDate_Depart() {
		return Date_Depart;
	}
	public void setDate_Depart(Date date_Depart) {
		Date_Depart = date_Depart;
	}
	public Date getDate_Arrive() {
		return Date_Arrive;
	}
	public void setDate_Arrive(Date date_Arrive) {
		Date_Arrive = date_Arrive;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
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
	public String getAller() {
		return Aller;
	}
	public void setAller(String aller) {
		Aller = aller;
	}
	public String getRetour() {
		return retour;
	}
	public void setRetour(String retour) {
		this.retour = retour;
	}
	public int getPrix() {
		return prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}
	public int getPrixpromo() {
		return prixpromo;
	}
	public void setPrixpromo(int prixpromo) {
		this.prixpromo = prixpromo;
	}
	public boolean isPromo() {
		return promo;
	}
	public void setPromo(boolean promo) {
		this.promo = promo;
	}
	 public Vol() {
	    }
	public Vol(Long id, Date date_Depart, Date date_Arrive, LocalTime heureDepartall, LocalTime heureArriveeall,
			LocalTime heureDepartarr, LocalTime heureArriveearr, String aller, String retour, String image, int prix,
			int prixpromo, boolean promo, Collection<Reservation> reservations, Admin admin) {
	
		this.id = id;
		Date_Depart = date_Depart;
		Date_Arrive = date_Arrive;
		this.heureDepartall = heureDepartall;
		this.heureArriveeall = heureArriveeall;
		this.heureDepartarr = heureDepartarr;
		this.heureArriveearr = heureArriveearr;
		Aller = aller;
		this.retour = retour;
		this.image = image;
		this.prix = prix;
		this.prixpromo = prixpromo;
		this.promo = promo;
		this.reservations = reservations;
		this.admin = admin;
	}

	   


}
