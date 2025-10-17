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
	private final ProductRepository productRepository;
	
	public CartService(CartRepository cartRepository, ProductRepository productRepository) {
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
	}
	
	public CartItem addToCart(CartItem cartItem) {
		return cartRepository.save(cartItem);
	}
	

	public CartSummaryDto getCartSummary(Long userId) {
	    List<CartItem> cartItems = cartRepository.findByUserId(userId);

	    double totalPrice = 0;
	    double totalCarbon = 0;
	    String ecoSuggestion = null;

	    for (CartItem item : cartItems) {
	        Product product = productRepository.findById(item.getProductId())
	                .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

	        totalPrice += product.getPrice() * item.getQuantity();
	        totalCarbon += product.getCarbonImpact() * item.getQuantity();

	        if (Boolean.FALSE.equals(product.getEcoCertified())) {
	        	String[] words = product.getName().split("");
	        	String keyword = words[words.length - 1].replaceAll("[^a-zA-Z]","");
	        	
	        	
	            Optional<Product> ecoAlt = productRepository
	                    .findFirstByEcoCertifiedTrueAndNameContainingIgnoreCase(keyword);

	            if (ecoAlt.isPresent()) {
	                double saved = product.getCarbonImpact() - ecoAlt.get().getCarbonImpact();

	                if (saved > 0.5) {
	                    ecoSuggestion = "💡 Switch to " + ecoAlt.get().getName()
	                            + " and save " + saved + " kg CO₂!";
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