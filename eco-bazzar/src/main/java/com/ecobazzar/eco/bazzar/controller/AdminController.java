package com.ecobazzar.eco.bazzar.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecobazzar.eco.bazzar.model.Product;
import com.ecobazzar.eco.bazzar.model.User;
import com.ecobazzar.eco.bazzar.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	
	private final AdminService adminService;
	
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}
	
	@PutMapping("/approveProduct/{id}")
	public Product approveProduct(@PathVariable Long id) {
		return adminService.approveProduct(id);
	}
	
	@PutMapping("/approveSeller/{id}")
	public User approveSeller(@PathVariable Long id) {
		return adminService.approveSeller(id);
	}
	
	@GetMapping("/users")
	public List<User> getAllUsers(){
		return adminService.getAllUsers();
	}
	
	@GetMapping("/reports")
	public Map<String, Object> getReports(){
		return adminService.getAdminReport();
	}
	
	@GetMapping("/reports/download")
		public String downloadReport() {
			return adminService.generateReportCVS();
		}
	
}
