package com.ecobazzar.eco.bazzar.repository;

import com.ecobazzar.eco.bazzar.model.AdminRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdminRequestRepository extends JpaRepository<AdminRequest, Long> {

    List<AdminRequest> findByApprovedFalseAndRejectedFalseOrderByRequestedAtDesc();
    boolean existsByUserIdAndApprovedFalseAndRejectedFalse(Long userId);
    long countByApprovedFalseAndRejectedFalse();
}
