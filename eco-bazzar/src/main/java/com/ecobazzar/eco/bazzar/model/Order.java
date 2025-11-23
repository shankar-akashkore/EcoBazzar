package com.ecobazzar.eco.bazzar.model;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "carbon_used")
    private Double carbonUsed;

    @Column(name = "carbon_saved")
    private Double carbonSaved;

    @Column(name = "total_carbon", nullable = false)
    private Double totalCarbon;

    @Column(name = "total_price")
    private Double totalPrice;

    public Order() {}

    public Order(Long id, Long userId, LocalDate orderDate,
                 Double carbonUsed, Double carbonSaved, Double totalCarbon, Double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.carbonUsed = carbonUsed;
        this.carbonSaved = carbonSaved;
        this.totalCarbon = totalCarbon;
        this.totalPrice = totalPrice;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public Double getCarbonUsed() { return carbonUsed; }
    public void setCarbonUsed(double carbonUsed) { this.carbonUsed = carbonUsed; }

    public Double getCarbonSaved() { return carbonSaved; }
    public void setCarbonSaved(double carbonSaved) { this.carbonSaved = carbonSaved; }

    public Double getTotalCarbon() { return totalCarbon; }
    public void setTotalCarbon(double totalCarbon) { this.totalCarbon = totalCarbon; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
