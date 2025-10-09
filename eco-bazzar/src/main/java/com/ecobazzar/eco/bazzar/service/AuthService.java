package com.ecobazzar.eco.bazzar.service;

import java.util.Optional;

import com.ecobazzar.eco.bazzar.model.User; 
import org.springframework.stereotype.Service;

import com.ecobazzar.eco.bazzar.dto.LoginRequest;
import com.ecobazzar.eco.bazzar.dto.RegisterRequest;
import com.ecobazzar.eco.bazzar.dto.UserRespone;
import com.ecobazzar.eco.bazzar.repository.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	
	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public UserRespone register(RegisterRequest request) {
		Optional<User> existing = userRepository.findByEmail(request.getEmail());
		if(existing.isPresent()) {
			throw new RuntimeException("Email is already taken");
		}
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setRole("CUSTOMER");
		user.setEcoScore(0);
		
		userRepository.save(user);
		
		return new UserRespone(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getEcoScore());
	}
	
	public UserRespone login(LoginRequest login) {
		User user = userRepository.findByEmail(login.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		if(!user.getPassword().equals(login.getPassword())) {
			throw new RuntimeException("Invalid Password");
		}
		
		return new UserRespone(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getEcoScore());
	}
}
