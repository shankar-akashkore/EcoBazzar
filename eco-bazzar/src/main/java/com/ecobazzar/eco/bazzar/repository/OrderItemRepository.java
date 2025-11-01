package com.ecobazzar.eco.bazzar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecobazzar.eco.bazzar.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	
	@Query("SELECT oi FROM OrderItem oi JOIN Product p ON oi.productId = p.id WHERE p.sellerId = :sellerId")
	List<OrderItem>findBySellerId(@Param("sellerId")Long sellerId);
	
	@Query("SELECT SUM(oi.quantity * p.price) FROM OrderItem oi JOIN Product p ON oi.productId = p.id WHERE p.sellerId = :sellerId")
	Double getTotalRevenueBySeller(@Param("sellerId") Long sellerId);
	
	@Query("SELECT SUM(oi.quantity * p.carbonImpact) FROM OrderItem oi JOIN Product p ON oi.productId = p.id WHERE p.sellerId = :sellerId")
	Double getTotalCarbonBySeller(@Param("sellerId") Long sellerId);
}
