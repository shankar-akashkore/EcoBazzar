package com.ecobazzar.eco.bazzar.controller;

import com.ecobazzar.eco.bazzar.model.User;
import com.ecobazzar.eco.bazzar.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/seller-request")
public class SellerRequestController {

    private final UserRepository userRepository;

    public SellerRequestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/request")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, String>> requestSellerRole(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, String> response = new HashMap<>();

        if ("ROLE_SELLER".equals(user.getRole())) {
            response.put("message", "You are already a seller");
            return ResponseEntity.badRequest().body(response);
        }
        if (user.isSellerRequestPending()) {
            response.put("message", "Request already pending");
            return ResponseEntity.badRequest().body(response);
        }

        user.setSellerRequestPending(true);
        userRepository.save(user);

        response.put("message", "Seller request sent successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/has-pending")
    @PreAuthorize("hasRole('USER')")
    public boolean hasPendingRequest(Authentication auth) {
        return userRepository.findByEmail(auth.getName())
                .map(User::isSellerRequestPending)
                .orElse(false);
    }
}
