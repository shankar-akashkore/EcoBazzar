package com.ecobazzar.eco.bazzar.model;

import java.time.LocalDate;

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

	private Long userId;
	
	private LocalDate orderDate;
	
	private double totalPrice;
	
	private double totalCarbon;
	
	public Order() {}

	public Order(Long id, Long userId, LocalDate orderDate, double totalPrice, double totalCarbon) {
		super();
		this.id = id;
		this.userId = userId;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
		this.totalCarbon = totalCarbon;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getTotalCarbon() {
		return totalCarbon;
	}

	public void setTotalCarbon(double totalCarbon) {
		this.totalCarbon = totalCarbon;
	}

	
	
}
