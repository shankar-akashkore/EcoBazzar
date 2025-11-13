package com.ecobazzar.eco.bazzar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "products")


public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String details;
	
	private Double price;
	
	private Double carbonImpact;
	
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean ecoCertified = false;
	
	private Long sellerId;
	
	private String imageURL;
	
	public Product() {}
		
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

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getCarbonImpact() {
		return carbonImpact;
	}

	public void setCarbonImpact(Double carbonImpact) {
		this.carbonImpact = carbonImpact;
	}

	public Boolean getEcoCertified() {
		return ecoCertified;
	}

	public void setEcoCertified(Boolean ecoCertified) {
		this.ecoCertified = ecoCertified;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	
	
	
}
