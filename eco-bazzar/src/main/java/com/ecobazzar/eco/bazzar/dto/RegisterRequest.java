package com.ecobazzar.eco.bazzar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

	
	@NotBlank(message = "Name is Required")
	private String name;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Enter a valid Mail")
	private String email;
	
	@NotBlank(message = "Password is required")
	@Size(min = 4 , message = "Password must be atleast 4 Characters")
	private String password;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
