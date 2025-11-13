package com.ecobazzar.eco.bazzar.dto;

public class UserReport {

	private Long userId;
	
	private String userName;
	
	private Long totalPurchses;
	
	private Double totalSpent;
	
	private Double totalCarbonSaved;
	
	private String ecoBadge;
	
	public UserReport() {
		
	}

	public UserReport(Long userId, String userName, Long totalPurchses, Double totalSpent, Double totalCarbonSaved,
			String ecoBadge) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.totalPurchses = totalPurchses;
		this.totalSpent = totalSpent;
		this.totalCarbonSaved = totalCarbonSaved;
		this.ecoBadge = ecoBadge;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getTotalPurchses() {
		return totalPurchses;
	}

	public void setTotalPurchses(Long totalPurchses) {
		this.totalPurchses = totalPurchses;
	}

	public Double getTotalSpent() {
		return totalSpent;
	}

	public void setTotalSpent(Double totalSpent) {
		this.totalSpent = totalSpent;
	}

	public Double getTotalCarbonSaved() {
		return totalCarbonSaved;
	}

	public void setTotalCarbonSaved(Double totalCarbonSaved) {
		this.totalCarbonSaved = totalCarbonSaved;
	}

	public String getEcoBadge() {
		return ecoBadge;
	}

	public void setEcoBadge(String ecoBadge) {
		this.ecoBadge = ecoBadge;
	}
	
	
}
