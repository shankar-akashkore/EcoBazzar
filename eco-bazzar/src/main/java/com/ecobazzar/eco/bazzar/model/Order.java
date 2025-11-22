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
    private double carbonUsed;

    @Column(name = "carbon_saved")
    private double carbonSaved;

    @Column(name = "total_carbon", nullable = false)
    private double totalCarbon;

    @Column(name = "total_price")
    private double totalPrice;

    public Order() {}

    public Order(Long id, Long userId, LocalDate orderDate,
                 double carbonUsed, double carbonSaved, double totalCarbon, double totalPrice) {
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

    public double getCarbonUsed() { return carbonUsed; }
    public void setCarbonUsed(double carbonUsed) { this.carbonUsed = carbonUsed; }

    public double getCarbonSaved() { return carbonSaved; }
    public void setCarbonSaved(double carbonSaved) { this.carbonSaved = carbonSaved; }

    public double getTotalCarbon() { return totalCarbon; }
    public void setTotalCarbon(double totalCarbon) { this.totalCarbon = totalCarbon; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
