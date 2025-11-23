package com.ecobazzar.eco.bazzar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecobazzar.eco.bazzar.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

    List<Order> findByUserId(Long userId);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.userId = :userId")
    Double getTotalSpendByUser(Long userId);

    @Query("SELECT SUM(o.carbonUsed) FROM Order o WHERE o.userId = :userId")
    Double getTotalCarbonUsed(Long userId);

    @Query("SELECT SUM(o.carbonSaved) FROM Order o WHERE o.userId = :userId")
    Double getTotalCarbonSaved(Long userId);
}
