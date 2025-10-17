package com.ecobazzar.eco.bazzar.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecobazzar.eco.bazzar.model.CartItem;
import com.ecobazzar.eco.bazzar.model.Order;
import com.ecobazzar.eco.bazzar.model.Product;
import com.ecobazzar.eco.bazzar.repository.CartRepository;
import com.ecobazzar.eco.bazzar.repository.OrderRepository;
import com.ecobazzar.eco.bazzar.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	
    private final CartRepository cartRepository;
	
	private final ProductRepository productRepository;
	
	private final OrderRepository orderRepository;
	
	public OrderService(CartRepository cartRepository, ProductRepository productRepository, OrderRepository orderRepository) {
		this.cartRepository  = cartRepository;
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
	}
	
	@Transactional
	public Order checkout(Long userId) {
		List<CartItem> cartItems = cartRepository.findByUserId(userId);
		
		if(cartItems.isEmpty()) {
			throw new RuntimeException("Cart is Empty! Cannot Checkout");
		}
			
			double totalPrice = 0;
			
			double totalCarbon = 0;
			
			for(CartItem item:cartItems) {
				Product product = productRepository.findById(item.getProductId())
						.orElseThrow(()-> new RuntimeException("Product not found"));
				
				totalPrice += product.getPrice()*item.getQuantity();
				totalCarbon += product.getCarbonImpact()*item.getQuantity();
				
		}	
			Order order = new Order(null, userId, LocalDate.now(), totalPrice, totalCarbon);
			
			Order savedOrder = orderRepository.save(order);
			
			cartRepository.deleteAll(cartItems);
			
			return savedOrder;
	}
	 
	public List<Order> getOrdersByUserId(Long userId){
		return orderRepository.findByUserId(userId);
	}
}
