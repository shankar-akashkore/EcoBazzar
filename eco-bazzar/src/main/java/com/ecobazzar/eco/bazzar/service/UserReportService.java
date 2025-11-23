package com.ecobazzar.eco.bazzar.service;

import org.springframework.stereotype.Service;
import com.ecobazzar.eco.bazzar.dto.UserReport;
import com.ecobazzar.eco.bazzar.model.User;
import com.ecobazzar.eco.bazzar.repository.OrderRepository;
import com.ecobazzar.eco.bazzar.repository.UserRepository;

@Service
public class UserReportService {

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    public UserReportService(UserRepository userRepository, OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public UserReport getUserReport(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long totalPurchase = (long) orderRepository.findByUserId(userId).size();

        Double totalSpent = orderRepository.getTotalSpendByUser(userId);
        if (totalSpent == null) totalSpent = 0.0;

        Double totalUsed = orderRepository.getTotalCarbonUsed(userId);
        if (totalUsed == null) totalUsed = 0.0;

        Double totalSaved = orderRepository.getTotalCarbonSaved(userId);
        if (totalSaved == null) totalSaved = 0.0;

        String badge = getEcoBadge(totalSaved);

        return new UserReport(
                user.getId(),
                user.getName(),
                totalPurchase,
                totalSpent,
                totalUsed,
                totalSaved,
                badge
        );
    }

    private String getEcoBadge(Double saved) {
        if (saved > 500) return "🌎 Eco Legend";
        if (saved > 200) return "🌿 Green Hero";
        if (saved > 100) return "🍃 Conscious Shopper";
        if (saved > 0)   return "🌱 Beginner Eco-Saver";
        return "🚫 No Eco Savings Yet";
    }
}
