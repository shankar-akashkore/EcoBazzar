package com.ecobazzar.eco.bazzar.dto;

public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String role;

    private Integer ecoScore;

    private String token;

    public UserResponse(Long id, String name, String email, String role, Integer ecoScore, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.ecoScore = ecoScore;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
