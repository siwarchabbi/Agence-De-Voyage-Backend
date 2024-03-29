package com.itgate.springjwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Client extends User{
    private String adress ;
    private String city ;
    private String image ;
    
    private String isactive ;
    private String isdelate ;
    
    @OneToMany( mappedBy = "client" , cascade = CascadeType.REMOVE)
	   private Collection<Reservation> reservations;
    @OneToMany( mappedBy = "client" , cascade = CascadeType.REMOVE)
	   private Collection<Favorie> favories;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Collection<Reclamation> reclamations = new ArrayList<>();
    
    @OneToMany(mappedBy = "client")
    private Collection<Commentaire> commentaires;

    
    public Client(String username, String email, String password, String adress, String city) {
        super(username, email, password);
        this.adress = adress;
        this.city = city;
    }
    public Client(){}

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


    @JsonIgnore
	public Collection<Reservation> getReservations() {
		return reservations;
	}
	public String getIsactive() {
		return isactive;
	}
	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}
	public String getIsdelate() {
		return isdelate;
	}
	public void setIsdelate(String isdelate) {
		this.isdelate = isdelate;
	}
	public void setReservations(Collection<Reservation> reservations) {
		this.reservations = reservations;
	}
	@JsonIgnore
	public Collection<Reclamation> getReclamations() {
		return reclamations;
	}
	public void setReclamations(Collection<Reclamation> reclamations) {
		this.reclamations = reclamations;
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
