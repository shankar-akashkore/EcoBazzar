package com.ecobazzar.eco.bazzar.service;

import java.util.Optional;

import com.ecobazzar.eco.bazzar.model.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecobazzar.eco.bazzar.dto.LoginRequest;
import com.ecobazzar.eco.bazzar.dto.RegisterRequest;
import com.ecobazzar.eco.bazzar.dto.UserRespone;
import com.ecobazzar.eco.bazzar.repository.UserRepository;
import com.ecobazzar.eco.bazzar.util.JwtUtil;

@Service
public class AuthService {
	
	private final UserRepository userRepository;
	
    private final PasswordEncoder passwordEncoder;
    
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        }
    public UserRespone register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }
        
        String role = "ROLE_USER";
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setEcoScore(0);

        User saved = userRepository.save(user);
        return new UserRespone(
        		saved.getId(),
        		saved.getName(), 
        		saved.getEmail(), 
        		saved.getRole(), 
        		0, null);
        }
    public UserRespone login(LoginRequest login) {
        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));
        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
        	throw new RuntimeException("Invalid credentials!");
        	}
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());
        return new UserRespone(
        				user.getId(),
        				user.getName(), 
        				user.getEmail(), 
        				user.getRole(), 
        				user.getEcoScore(), 
        				token);
        }
}
