package com.ecobazzar.eco.bazzar.controller;

import com.ecobazzar.eco.bazzar.model.User;
import com.ecobazzar.eco.bazzar.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.ecobazzar.eco.bazzar.service.AdminRequestService;
import com.ecobazzar.eco.bazzar.dto.PendingAdminRequestDto;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/admin-request")
public class AdminRequestController {

    private final AdminRequestService service;
    private final UserRepository userRepository;

    public AdminRequestController(AdminRequestService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @PostMapping("/request")
    public ResponseEntity<Map<String, String>> requestAccess(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, String> response = new HashMap<>();
        try {
            service.requestAdminAccess(user.getId());
            response.put("message", "Admin access requested successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg.contains("already an admin") || msg.contains("pending admin request")) {
                response.put("message", msg);
                return ResponseEntity.status(409).body(response);
            }
            throw e;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending")
    public List<PendingAdminRequestDto> getPending() {
        return service.getPendingRequests().stream()
                .map(req -> new PendingAdminRequestDto(
                        req.getId(),
                        req.getUser().getId(),
                        req.getUser().getName(),
                        req.getUser().getEmail(),
                        req.getRequestedAt()
                ))
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve/{id}")
    public ResponseEntity<Map<String, String>> approve(@PathVariable Long id) {
        service.approveRequest(id);
        Map<String, String> res = new HashMap<>();
        res.put("message", "User promoted to Admin successfully");
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reject/{id}")
    public ResponseEntity<Map<String, String>> reject(@PathVariable Long id) {
        service.rejectRequest(id);
        Map<String, String> res = new HashMap<>();
        res.put("message", "Admin request rejected");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/has-pending")
    public boolean hasPending() {
        return service.hasPendingRequests();
    }
}
