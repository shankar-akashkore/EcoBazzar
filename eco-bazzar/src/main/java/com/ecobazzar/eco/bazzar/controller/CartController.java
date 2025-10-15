package com.ecobazzar.eco.bazzar.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecobazzar.eco.bazzar.dto.CartSummaryDto;
import com.ecobazzar.eco.bazzar.model.CartItem;
import com.ecobazzar.eco.bazzar.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final CartService cartService;
	
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}
	
	@PostMapping
	public CartItem addToCart(@RequestBody CartItem cartItem) {
		return cartService.addToCart(cartItem);
	}
	
	@GetMapping("/{userId}")
	public CartSummaryDto getCartSummary(@PathVariable Long userId) {
		return cartService.getCartSummary(userId);
	}
	
	@DeleteMapping("/{id}")
	public String removeFromCart(@PathVariable Long id) {
		cartService.removeFromCart(id);
		return "Item removed from Cart";
	}
	
}
