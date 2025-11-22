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

@RestController
@RequestMapping("/api/seller-request")
public class SellerRequestController {

    private final UserRepository userRepository;

    public SellerRequestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/request")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> requestSellerRole(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("ROLE_SELLER".equals(user.getRole())) {
            return ResponseEntity.badRequest().body("You are already a seller");
        }
        if (user.isSellerRequestPending()) {
            return ResponseEntity.badRequest().body("Request already pending");
        }

        user.setSellerRequestPending(true);
        userRepository.save(user);

        return ResponseEntity.ok("Seller request sent successfully");
    }

    @GetMapping("/has-pending")
    @PreAuthorize("hasRole('USER')")
    public boolean hasPendingRequest(Authentication auth) {
        return userRepository.findByEmail(auth.getName())
                .map(User::isSellerRequestPending)
                .orElse(false);
    }
}
