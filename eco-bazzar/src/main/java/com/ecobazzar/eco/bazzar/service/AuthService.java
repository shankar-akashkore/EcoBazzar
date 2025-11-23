package com.ecobazzar.eco.bazzar.service;

import com.ecobazzar.eco.bazzar.dto.UserResponse;
import com.ecobazzar.eco.bazzar.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ecobazzar.eco.bazzar.dto.LoginRequest;
import com.ecobazzar.eco.bazzar.dto.RegisterRequest;
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

    public UserResponse register(RegisterRequest request) {
        System.out.println("REGISTER ATTEMPT → Email: " + request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        String role = "ROLE_USER";

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // BCrypt
        user.setRole(role);
        user.setEcoScore(0);

        User saved = userRepository.save(user);

        System.out.println("REGISTER SUCCESS → User saved with ID: " + saved.getId() +
                " | Role: " + saved.getRole() +
                " | Password hash: " + saved.getPassword());

        return new UserResponse(saved.getId(), saved.getName(), saved.getEmail(), saved.getRole(), 0, null);
    }

    public UserResponse login(LoginRequest login) {
        String email = login.getEmail();
        String rawPassword = login.getPassword();

        System.out.println("==================================================");
        System.out.println("LOGIN ATTEMPT → Email: " + email);
        System.out.println("Raw password received: " + rawPassword);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("LOGIN FAILED → User not found in database!");
                    return new RuntimeException("User not found!");
                });

        System.out.println("User found → ID: " + user.getId() +
                " | Name: " + user.getName() +
                " | Role: " + user.getRole());

        String storedHash = user.getPassword();
        System.out.println("Stored password hash: " + storedHash);

        boolean passwordMatches = passwordEncoder.matches(rawPassword, storedHash);
        System.out.println("BCrypt password match result: " + passwordMatches);

        if (!passwordMatches) {
            System.out.println("LOGIN FAILED → Invalid credentials for " + email);
            throw new RuntimeException("Invalid credentials!");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());
        System.out.println("LOGIN SUCCESS → JWT generated: " + token.substring(0, 20) + "...");
        System.out.println("==================================================");

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getEcoScore(),
                token
        );
    }
}
