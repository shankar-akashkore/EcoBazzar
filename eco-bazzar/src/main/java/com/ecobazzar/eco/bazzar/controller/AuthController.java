package com.ecobazzar.eco.bazzar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecobazzar.eco.bazzar.dto.UserRespone;
import com.ecobazzar.eco.bazzar.dto.LoginRequest;
import com.ecobazzar.eco.bazzar.dto.RegisterRequest;
import com.ecobazzar.eco.bazzar.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {

	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserRespone> register (@Valid @RequestBody RegisterRequest register) {
		return ResponseEntity.ok(authService.register(register));
	}
	
	@PostMapping("/login")
	public ResponseEntity<UserRespone> login(@Valid @RequestBody LoginRequest login) {
		return ResponseEntity.ok(authService.login(login));
	}
}
