package com.ecobazzar.eco.bazzar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
	
	@NotBlank(message= "Name is Required")
	private String name;

	@NotBlank(message = "EMail is Required")
	@Email(message = "Enter a valid email")
	private String email;


	@NotBlank(message = "Password is required")
	@Size(min = 4, message = "Password must be 4 characters")
	private String password;

	private String role;

	public RegisterRequest() {}

	public RegisterRequest(@NotBlank(message = "Name is Required") String name,
			@NotBlank(message = "EMail is Required") @Email(message = "Enter a valid email") String email,
			@NotBlank(message = "Password is required") @Size(min = 4, message = "Password must be 4 characters") String password,
			String role) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
		
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
