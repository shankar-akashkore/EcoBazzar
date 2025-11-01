package com.ecobazzar.eco.bazzar.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecobazzar.eco.bazzar.dto.SellerReport;
import com.ecobazzar.eco.bazzar.model.Product;
import com.ecobazzar.eco.bazzar.model.User;
import com.ecobazzar.eco.bazzar.repository.OrderItemRepository;
import com.ecobazzar.eco.bazzar.repository.ProductRepository;
import com.ecobazzar.eco.bazzar.repository.UserRepository;

@Service
public class SellerReportService {

	private final ProductRepository productRepository;
	
	private final OrderItemRepository orderItemRepository;
	
	private final UserRepository userRepository;
	
	public SellerReportService(ProductRepository productRepository, OrderItemRepository orderItemRepository, UserRepository userRepository) {
		this.productRepository = productRepository;
		this.orderItemRepository = orderItemRepository;
		this.userRepository = userRepository;
	}
	
	public SellerReport getSellerReport(String email) {
		User seller = userRepository.findByEmail(email)
				.orElseThrow(()-> new RuntimeException("Seller Not Found"));
		
		Long sellerId = seller.getId();
		List<Product> products = productRepository.findAll();
		long totalProducts = products.stream().filter(p->sellerId.equals(p.getSellerId())).count();
		long totalEcoCertified = products.stream().filter(p->sellerId.equals(p.getSellerId())&&Boolean.TRUE.equals(p.getEcoCertified())).count();
		Double totalRevenue = orderItemRepository.getTotalRevenueBySeller(sellerId);
		Double totalCarbon = orderItemRepository.getTotalCarbonBySeller(sellerId);
		
		if(totalRevenue == null) {
			totalRevenue = 0.0;
		}
		
		if(totalCarbon == null) {
			totalCarbon = 0.0;
		}
		
		Long totalOrders = (long)orderItemRepository.findBySellerId(sellerId).size();
		
		return new SellerReport(
			sellerId,
			seller.getName(),
			totalProducts,
			totalEcoCertified,
			totalOrders,
			totalCarbon,
			totalRevenue);
		
	}
}
