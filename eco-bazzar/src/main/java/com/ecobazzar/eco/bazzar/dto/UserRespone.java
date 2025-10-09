package com.ecobazzar.eco.bazzar.dto;

public class UserRespone {

	private Long id;
	
	private String name;
	
	private String email;
	
	private String role;
	
	private Integer ecoScore;
	
	public UserRespone(Long id, String name, String email, String role, Integer ecoScore) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
		this.ecoScore = ecoScore;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getEcoScore() {
		return ecoScore;
	}

	public void setEcoScore(Integer ecoScore) {
		this.ecoScore = ecoScore;
	}
	
}
