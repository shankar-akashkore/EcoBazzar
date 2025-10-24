package com.ecobazzar.eco.bazzar.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecobazzar.eco.bazzar.dto.UserReport;
import com.ecobazzar.eco.bazzar.service.UserReportService;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*")
public class UserReportController {

	
	private final UserReportService userReportService;
	
	public UserReportController(UserReportService userReportService) {
		this.userReportService = userReportService;
	}
	
	@GetMapping("/user/{id}")
	public UserReport getUserReport(@PathVariable Long id) {
		return userReportService.getUserReport(id);
	}
	
}
