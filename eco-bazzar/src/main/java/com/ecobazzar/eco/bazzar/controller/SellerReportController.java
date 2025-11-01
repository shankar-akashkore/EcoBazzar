package com.ecobazzar.eco.bazzar.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecobazzar.eco.bazzar.dto.SellerReport;
import com.ecobazzar.eco.bazzar.service.SellerReportService;

@RestController
@RequestMapping("/api/reports")
public class SellerReportController {

	private final SellerReportService sellerReportService;
	
	public SellerReportController(SellerReportService sellerReportService) {
		this.sellerReportService = sellerReportService;
	}
	
	@PreAuthorize("hasRole('SELLER')")
	@GetMapping("/seller")
	public SellerReport getSellerReport(Authentication auth) {
		String email = auth.name();
		return sellerReportService.getSellerReport(email);
	}
} 
