package com.ecobazzar.eco.bazzar.service;

import com.ecobazzar.eco.bazzar.model.AdminRequest;
import com.ecobazzar.eco.bazzar.model.User;
import com.ecobazzar.eco.bazzar.repository.AdminRequestRepository;
import com.ecobazzar.eco.bazzar.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AdminRequestService {

    private final AdminRequestRepository adminRequestRepo;

    private final UserRepository userRepo;

    public AdminRequestService(AdminRequestRepository adminRequestRepo, UserRepository userRepo) {
        this.adminRequestRepo = adminRequestRepo;
        this.userRepo = userRepo;
    }

    public void requestAdminAccess(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("ROLE_ADMIN".equals(user.getRole())) {
            throw new RuntimeException("You are already an admin");
        }

        if (adminRequestRepo.existsByUserIdAndApprovedFalseAndRejectedFalse(userId)) {
            throw new RuntimeException("You already have a pending admin request");
        }

        AdminRequest request = new AdminRequest();
        request.setUser(user);
        request.setRequestedAt(LocalDateTime.now());
        adminRequestRepo.save(request);
    }

    public List<AdminRequest> getPendingRequests() {
        return adminRequestRepo.findByApprovedFalseAndRejectedFalseOrderByRequestedAtDesc();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void approveRequest(Long requestId) {
        AdminRequest req = adminRequestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (req.isApproved() || req.isRejected()) {
            throw new RuntimeException("Request already processed");
        }

        User user = req.getUser();
        user.setRole("ROLE_ADMIN");
        userRepo.save(user);

        req.setApproved(true);
        req.setProcessedAt(LocalDateTime.now());
        adminRequestRepo.save(req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void rejectRequest(Long requestId) {
        AdminRequest req = adminRequestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        req.setRejected(true);
        req.setProcessedAt(LocalDateTime.now());
        adminRequestRepo.save(req);
    }

    public boolean hasPendingRequests() {
        return adminRequestRepo.countByApprovedFalseAndRejectedFalse() > 0;
    }
}
