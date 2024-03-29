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
import jakarta.validation.constraints.AssertFalse.List;

@Entity
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")	
	private Date date_debutr;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date date_finr ;
	private int prix;
	 private String status;
	 
	
	 
	
	 @ManyToMany
	    @JoinTable(
	        name = "reservation_hotel",
	        joinColumns = @JoinColumn(name = "reservation_id"),
	        inverseJoinColumns = @JoinColumn(name = "hotel_id")
	    )
	    private Collection<Hotel> hotels;
	
	 @ManyToMany
	   @JoinTable(name = "reservation_vehicule",
	   joinColumns = @JoinColumn(name = "reservation_id")
	   ,inverseJoinColumns = @JoinColumn(name="vehicule_id"))
	   private Collection<Vehicule> vehicules;
	 @ManyToMany
	   @JoinTable(name = "reservation_vol",
	   joinColumns = @JoinColumn(name = "reservation_id")
	   ,inverseJoinColumns = @JoinColumn(name="vol_id"))
	   private Collection<Vol> vols;
	@ManyToOne
	   @JoinColumn(name = "client_id")
	   private Client client;
	private String hot_id;
	private String vo_id;
	private String ve_id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDate_debutr() {
		return date_debutr;
	}
	public void setDate_debutr(Date date_debutr) {
		this.date_debutr = date_debutr;
	}
	public Date getDate_finr() {
		return date_finr;
	}
	public void setDate_finr(Date date_finr) {
		this.date_finr = date_finr;
	}
	
	
	
	public int getPrix() {
		return prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}
	@JsonIgnore
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
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
	
	
	
	
	 public void addHotel(Hotel h){
	        if(hotels==null){
	        	hotels=new ArrayList<>();
	        }
	        hotels.add(h);
	    }
	
	 public void addVol(Vol v){
	        if(vols==null){
	        	vols=new ArrayList<>();
	        }
	        vols.add(v);
	    }
	public void addVehicule(Vehicule r1) {
		// TODO Auto-generated method stub
		 if(vehicules==null){
	        	vehicules=new ArrayList<>();
	        }
	        vehicules.add(r1);
		
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Reservation(Long id, Date date_debutr, Date date_finr,String hot_id,String vo_id, String ve_id,  int prix, String status, Collection<Hotel> hotels,
			Collection<Vehicule> vehicules, Collection<Vol> vols, Client client ) {
	
		this.id = id;
		this.date_debutr = date_debutr;
		this.date_finr = date_finr;
		this.prix = prix;
		this.status = status;
		this.hotels = hotels;
		this.vehicules = vehicules;
		this.vols = vols;
		this.client = client;
		this.hot_id = hot_id;
		this.vo_id = vo_id;
		this.ve_id = ve_id;
		
	}
	
	public Reservation() {
		
	}
	public String getHot_id() {
		return hot_id;
	}
	public void setHot_id(String hot_id) {
		this.hot_id = hot_id;
	}
	public String getVo_id() {
		return vo_id;
	}
	public void setVo_id(String vo_id) {
		this.vo_id = vo_id;
	}
	public String getVe_id() {
		return ve_id;
	}
	public void setVe_id(String ve_id) {
		this.ve_id = ve_id;
	}
	

	
	

}
