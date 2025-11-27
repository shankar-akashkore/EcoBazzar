package com.ecobazzar.eco.bazzar.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecobazzar.eco.bazzar.model.Order;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long>{

    List<Order> findByUserId(Long userId);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.userId = :userId")
    Double getTotalSpendByUser(@Param("userId") Long userId);

    @Query("SELECT SUM(o.carbonUsed) FROM Order o WHERE o.userId = :userId")
    Double getTotalCarbonUsed(@Param("userId") Long userId);

    @Query("SELECT SUM(o.carbonSaved) FROM Order o WHERE o.userId = :userId")
    Double getTotalCarbonSaved(@Param("userId") Long userId);

    @Query(value = "SELECT DATE(o.order_date), COALESCE(SUM(o.carbon_saved), 0) " +
            "FROM orders o WHERE o.user_id = :userId AND o.order_date BETWEEN :start AND :end " +
            "GROUP BY DATE(o.order_date) ORDER BY DATE(o.order_date)", nativeQuery = true)
    List<Object[]> getDailyCarbonSaved(@Param("userId") Long userId,
                                       @Param("start") Date start, @Param("end") Date end);

    @Query(value = "SELECT DATE(o.order_date), COALESCE(SUM(o.carbon_used), 0) " +
            "FROM orders o WHERE o.user_id = :userId AND o.order_date BETWEEN :start AND :end " +
            "GROUP BY DATE(o.order_date) ORDER BY DATE(o.order_date)", nativeQuery = true)
    List<Object[]> getDailyCarbonUsed(@Param("userId") Long userId,
                                      @Param("start") Date start, @Param("end") Date end);
}
