package com.ecobazzar.eco.bazzar.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecobazzar.eco.bazzar.model.Order;
import com.ecobazzar.eco.bazzar.model.User;
import com.ecobazzar.eco.bazzar.repository.UserRepository;
import com.ecobazzar.eco.bazzar.service.OrderService;

@RestController
@RequestMapping("api/orders")
public class OrderController {

	
    private final OrderService orderService;
    
    private final UserRepository userRepository;
	
	public OrderController(OrderService orderService, UserRepository userRepository) {
		this.orderService = orderService;
		this.userRepository = userRepository;
	}
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/checkout")
	public Order checkout() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User currentUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found"));
		return orderService.checkout(currentUser.getId());
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping
	public List<Order> getUserOrder(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User currentUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found"));
		return orderService.getOrdersByUserId(currentUser.getId());
	}
}
