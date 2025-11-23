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
        SELECT COALESCE(SUM(oi.quantity * COALESCE(p.price, 0)), 0)
        FROM order_items oi
        JOIN products p ON oi.product_id = p.id
        WHERE p.seller_id = :sellerId
        """, nativeQuery = true)
    Double getTotalRevenueBySeller(@Param("sellerId") Long sellerId);

    @Query(value = """
        SELECT COALESCE(SUM(oi.quantity * COALESCE(p.carbon_impact, 0)), 0)
        FROM order_items oi
        JOIN products p ON oi.product_id = p.id
        WHERE p.seller_id = :sellerId
        """, nativeQuery = true)
    Double getTotalCarbonBySeller(@Param("sellerId") Long sellerId);

    @Query(value = """
    		  SELECT DATE(o.order_date) AS day,
    		         COALESCE(SUM(oi.quantity * COALESCE(p.price,0)),0) AS revenue
    		  FROM order_items oi
    		  JOIN orders o ON oi.order_id = o.id
    		  JOIN products p ON oi.product_id = p.id
    		  WHERE p.seller_id = :sellerId
    		    AND o.order_date >= DATE_SUB(CURDATE(), INTERVAL :days DAY)
    		  GROUP BY DATE(o.order_date)
    		  ORDER BY DATE(o.order_date)
    		""", nativeQuery = true)
    List<Object[]> getDailyRevenueBySeller(@Param("sellerId") Long sellerId, @Param("days") int days);

    @Query(value = """
    			      SELECT DATE(o.order_date) AS day,
    			             COALESCE(SUM(oi.quantity * COALESCE(p.price,0)), 0) AS revenue
    			      FROM order_items oi
    			      JOIN orders o ON oi.order_id = o.id
    			      JOIN products p ON oi.product_id = p.id
    			      WHERE p.seller_id = :sellerId
    			        AND o.order_date >= :sinceDate
    			      GROUP BY DATE(o.order_date)
    			      ORDER BY DATE(o.order_date)
    			      """, nativeQuery = true)
    List<Object[]> getDailyRevenueBySellerSince(@Param("sellerId") Long sellerId,
                                                @Param("sinceDate") java.sql.Date sinceDate);

}
