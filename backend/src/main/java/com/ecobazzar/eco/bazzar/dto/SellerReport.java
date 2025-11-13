package com.ecobazzar.eco.bazzar.dto;

public class SellerReport {

	
	private Long sellerId;
	
	private String sellerName;
	
	private Long totalProducts;
	
	private Long totalEcoCertified;
	
	private Long totalOrders;
	
	private Double totalRevenue;
	
	private Double totalCarbon;
	
	public SellerReport() {}

	public SellerReport(Long sellerId, String sellerName, Long totalProducts, Long totalEcoCertified, Long totalOrders,
			Double totalRevenue, Double totalCarbon) {
		super();
		this.sellerId = sellerId;
		this.sellerName = sellerName;
		this.totalProducts = totalProducts;
		this.totalEcoCertified = totalEcoCertified;
		this.totalOrders = totalOrders;
		this.totalRevenue = totalRevenue;
		this.totalCarbon = totalCarbon;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public Long getTotalProducts() {
		return totalProducts;
	}

	public void setTotalProducts(Long totalProducts) {
		this.totalProducts = totalProducts;
	}

	public Long getTotalEcoCertified() {
		return totalEcoCertified;
	}

	public void setTotalEcoCertified(Long totalEcoCertified) {
		this.totalEcoCertified = totalEcoCertified;
	}

	public Long getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(Long totalOrders) {
		this.totalOrders = totalOrders;
	}

	public Double getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(Double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public Double getTotalCarbon() {
		return totalCarbon;
	}

	public void setTotalCarbon(Double totalCarbon) {
		this.totalCarbon = totalCarbon;
	}
	
}
