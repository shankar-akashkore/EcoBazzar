package com.ecobazzar.eco.bazzar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecobazzar.eco.bazzar.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	
	 @Query(value = """
		        SELECT oi.id, oi.order_id, oi.product_id, oi.quantity
		        FROM order_items oi
		        JOIN products p ON oi.product_id = p.id
		        WHERE p.seller_id = :sellerId
		        """, nativeQuery = true)
		    List<OrderItem> findBySellerId(@Param("sellerId") Long sellerId);

		    @Query(value = """
		        SELECT COALESCE(SUM(oi.quantity * p.price), 0)
		        FROM order_items oi
		        JOIN products p ON oi.product_id = p.id
		        WHERE p.seller_id = :sellerId
		        """, nativeQuery = true)
		    Double getTotalRevenueBySeller(@Param("sellerId") Long sellerId);

		    @Query(value = """
		        SELECT COALESCE(SUM(oi.quantity * p.carbon_impact), 0)
		        FROM order_items oi
		        JOIN products p ON oi.product_id = p.id
		        WHERE p.seller_id = :sellerId
		        """, nativeQuery = true)
		    Double getTotalCarbonBySeller(@Param("sellerId") Long sellerId);
}
