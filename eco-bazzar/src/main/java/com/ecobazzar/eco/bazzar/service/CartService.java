package com.ecobazzar.eco.bazzar.service;

import java.util.List;
import java.util.Optional;
import jakarta.transaction.Transactional;
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
        List<CartItem> items = cartRepository.findByUserId(userId);

        double totalPrice = 0.0;
        double totalCarbonUsed = 0.0;
        double totalCarbonSaved = 0.0;

        CartSummaryDto.EcoSwapSuggestion swapSuggestion = generateSwapSuggestion(items);
        String ecoMessage = null;

        for (CartItem item : items) {
            Product current = productRepository.findById(item.getProductId()).orElse(null);
            if (current == null) continue;

            int qty = item.getQuantity();
            double currentImpact = current.getCarbonImpact() != null ? current.getCarbonImpact() : 0.0;

            totalPrice += (current.getPrice() != null ? current.getPrice() : 0.0) * qty;
            totalCarbonUsed += currentImpact * qty;

            String keyword = extractKeyword(current.getName());
            List<Product> allProducts = productRepository.findAll();

            double maxSimilarImpact = currentImpact;
            for (Product other : allProducts) {
                if (other.getId().equals(current.getId())) continue;
                if (other.getCarbonImpact() == null) continue;

                String name = other.getName() != null ? other.getName().toLowerCase() : "";
                if (name.contains(keyword)) {
                    // find a higher-carbon version of same-type product
                    if (other.getCarbonImpact() > maxSimilarImpact) {
                        maxSimilarImpact = other.getCarbonImpact();
                    }
                }
            }

            if (maxSimilarImpact > currentImpact) {
                double savedPerUnit = maxSimilarImpact - currentImpact;
                totalCarbonSaved += savedPerUnit * qty;
            }
        }

        if (swapSuggestion != null) {
            double totalSavings = swapSuggestion.getCarbonSavingsPerUnit() * swapSuggestion.getQuantity();
            ecoMessage = "Switch to " + swapSuggestion.getSuggestedProductName() +
                    " and save " + String.format("%.2f", totalSavings) + " kg CO₂!";
        }

        return new CartSummaryDto(
                items,
                totalPrice,
                totalCarbonUsed,
                totalCarbonSaved,
                ecoMessage,
                swapSuggestion
        );
    }

    public void removeFromCart(Long cartItemId, Long userId) {
        CartItem item = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!item.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        cartRepository.deleteById(cartItemId);
    }

    @Transactional
    public void swapToEco(Long userId, Long cartItemId, Long newProductId) {
        CartItem item = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        Product newProduct = productRepository.findById(newProductId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!newProduct.getEcoCertified()) {
            throw new RuntimeException("Can only swap to eco-certified product");
        }

        item.setProductId(newProductId);
        cartRepository.save(item);
    }

    private CartSummaryDto.EcoSwapSuggestion generateSwapSuggestion(List<CartItem> items) {
        Optional<CartItem> nonEcoItem = items.stream()
                .filter(i -> {
                    Product p = productRepository.findById(i.getProductId()).orElse(null);
                    return p != null && !p.getEcoCertified();
                })
                .findFirst();

        if (nonEcoItem.isEmpty()) return null;

        CartItem item = nonEcoItem.get();
        Product current = productRepository.findById(item.getProductId()).orElse(null);
        if (current == null || current.getCarbonImpact() == null) return null;

        String keyword = extractKeyword(current.getName());
        Optional<Product> ecoAlt = productRepository
                .findFirstByEcoCertifiedTrueAndNameContainingIgnoreCase(keyword);

        if (ecoAlt.isEmpty()) return null;

        Product eco = ecoAlt.get();
        if (eco.getCarbonImpact() == null) return null;

        double savingsPerUnit = current.getCarbonImpact() - eco.getCarbonImpact();
        if (savingsPerUnit <= 0) return null;

        var suggestion = new CartSummaryDto.EcoSwapSuggestion();
        suggestion.setCartItemIdToReplace(item.getId());
        suggestion.setSuggestedProductId(eco.getId());
        suggestion.setSuggestedProductName(eco.getName());
        suggestion.setCarbonSavingsPerUnit(savingsPerUnit);
        suggestion.setQuantity(item.getQuantity());

        return suggestion;
    }

    private String extractKeyword(String name) {
        if (name == null || name.isBlank()) return "";
        name = name.toLowerCase();
        name = name.replaceAll("\\b(eco|friendly|organic|natural|bio|premium|certified|green|kg|g|pack|litre|l|ml)\\b", "");
        name = name.replaceAll("[^a-z\\s]", " ").replaceAll("\\s+", " ").trim();
        String[] words = name.split("\\s+");
        return words.length > 0 ? words[words.length - 1] : name;
    }
}