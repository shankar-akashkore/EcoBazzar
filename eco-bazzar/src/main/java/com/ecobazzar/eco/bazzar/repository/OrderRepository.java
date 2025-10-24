package com.ecobazzar.eco.bazzar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecobazzar.eco.bazzar.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

	List<Order> findByUserId(Long userId);
	
	@Query("SELECT SUM(o.totalPrice)FROM Order o WHERE o.userId = :userId")
	Double getTotalSpentByUser(Long usedId);
	
	@Query("SELECT SUM(o.totalCarbon)FROM Order o WHERE o.userId = :userId")
	Double getTotalCarbonByUser(Long userId);
	
}
