package com.ecobazzar.eco.bazzar.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecobazzar.eco.bazzar.dto.UserReport;
import com.ecobazzar.eco.bazzar.model.User;
import com.ecobazzar.eco.bazzar.repository.UserRepository;
import com.ecobazzar.eco.bazzar.service.UserReportService;

@RestController
@RequestMapping("api/reports")

public class UserReportController {

	
	private final UserReportService userReportService;
	
	private final UserRepository userRepository;
	
	public UserReportController(UserReportService userReportService, UserRepository userRepository) {
		this.userReportService = userReportService;
		this.userRepository = userRepository;
	}
	
	@PreAuthorize("harRole('USER')")
	@GetMapping("/user")
	public UserReport getUserReport() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User currentUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Seller not found"));
		return userReportService.getUserReport(currentUser.getId());
	}
}
