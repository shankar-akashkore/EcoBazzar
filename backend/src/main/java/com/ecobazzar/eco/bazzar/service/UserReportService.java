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
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
	}
	
	public UserReport getUserReport(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		
		Long totalPurchases = (long) orderRepository.findByUserId(userId).size();
		Double totalSpent = orderRepository.getTotalSpentByUser(userId);
		Double totalCarbon = orderRepository.getTotalCarbonByUser(userId);
		
		if(totalSpent == null) {
			totalSpent = 0.0;
		}
		
		if(totalCarbon == null) {
			totalCarbon = 0.0;
		}
		
		String badge = getEcoBadge(totalCarbon);
		
		return new UserReport(
				user.getId(),
				user.getName(),
				totalPurchases,
				totalSpent,
				totalCarbon,
				badge);
	}
	
	private String getEcoBadge(Double carbonSaved) {
		if(carbonSaved > 500) {
			return " 🌏 Eco Lengend";
		} else if(carbonSaved > 200) {
			return " 🌿 Green Hero";
		} else if(carbonSaved > 100) {
			return " 🍃 Conscious Shopper";
		} else if(carbonSaved > 0) {
			return " 🌴 Beginner Eco-Saver";
		} else {
			return " 🚫 No Impact Yet";
		}
	}
	
}
