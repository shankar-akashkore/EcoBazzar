package com.ecobazzar.eco.bazzar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecobazzar.eco.bazzar.dto.CartSummaryDto;
import com.ecobazzar.eco.bazzar.model.CartItem;
import com.ecobazzar.eco.bazzar.model.Product;
import com.ecobazzar.eco.bazzar.repository.CartRepository;
import com.ecobazzar.eco.bazzar.repository.ProductRepository;

@Service
public class CartService {

	private final CartRepository cartRepository;
	
	public CartService(CartRepository cartRepository) {
		this.cartRepository = cartRepository;
	}
	
	
	public CartItem addToCart(CartItem cartItem) {
		return cartRepository.save(cartItem);
	}
	
	public CartSummaryDto getCartSummary(Long userId) {
		List<CartItem> cartItems = cartRepository.findByUserId(userId);
		
		double totalPrice = 0;
		double totalCarbon = 0;
		String ecoSuggestion = null;
		
		for(CartItem item: cartItems) {
			Product product = productRepository.findById(item.getId())
					.orElseThrow(() -> new RuntimeException("Product not found:" + item.getProductId()));
			
			
			totalPrice = product.getPrice() * item.getQuantity();
			totalCarbon = product.getCarbonImpact() * item.getQuantity();
			
			
			if(!product.getEcoCertified()) {
				Optional<Product> ecoAlt = ProductRepository
						.findByEcoCertifiedTrue(product.getName().split("")[0]);
				
				
				if(ecoAlt.isPresent()) {
					double saved = product.getCarbonImpact() - ecoAlt.getCarbonImpact();
					
					
					
					if(saved > 0.5) {
						ecoSuggestion = "💡 Switch to " + ecoAlt.get().getName()
								+ "andsave" + saved + "kg Co2";
					}
				}
			}
		}
		return new CartSummaryDto(cartItems, totalPrice, totalCarbon, ecoSuggestion);
	}
	
	
	public void removeFromCart(Long id) {
		cartRepository.deleteById(id);
	}
	
}
