package com.ecobazzar.eco.bazzar.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.ecobazzar.eco.bazzar.model.CartItem;
import com.ecobazzar.eco.bazzar.model.Order;
import com.ecobazzar.eco.bazzar.model.OrderItem;
import com.ecobazzar.eco.bazzar.model.Product;
import com.ecobazzar.eco.bazzar.repository.CartRepository;
import com.ecobazzar.eco.bazzar.repository.OrderItemRepository;
import com.ecobazzar.eco.bazzar.repository.OrderRepository;
import com.ecobazzar.eco.bazzar.repository.ProductRepository;
import jakarta.transaction.Transactional;

@Service
public class OrderService {


    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    public OrderService(CartRepository cartRepository,
                        ProductRepository productRepository,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public Order checkout(Long userId) {
        List<CartItem> cartItems = cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is Empty! Cannot Checkout");
        }

        double totalPrice = 0.0;
        double totalCarbonUsed = 0.0;
        double totalCarbonSaved = 0.0;

        for (CartItem item : cartItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            double price = product.getPrice() != null ? product.getPrice() : 0.0;
            double carbon = product.getCarbonImpact() != null ? product.getCarbonImpact() : 0.0;

            totalPrice += price * item.getQuantity();
            totalCarbonUsed += carbon * item.getQuantity();

            if (Boolean.TRUE.equals(product.getEcoCertified())) {
                List<Product> nonEcoCandidates = productRepository.findByEcoCertifiedFalse();

                Optional<Product> matchedConventional = nonEcoCandidates.stream()
                        .filter(p -> {
                            String ecoKey = extractKeyword(product.getName());
                            String nonEcoKey = extractKeyword(p.getName());
                            String ecoName = product.getName().toLowerCase();
                            String nonEcoName = p.getName().toLowerCase();

                            return ecoKey.equals(nonEcoKey) ||
                                    ecoName.contains(nonEcoKey) ||
                                    nonEcoName.contains(ecoKey);
                        })
                        .min(Comparator.comparingDouble(p -> {
                            double otherCarbon = p.getCarbonImpact() != null ? p.getCarbonImpact() : 0.0;
                            return Math.abs(otherCarbon - carbon);
                        }));

                if (matchedConventional.isPresent()) {
                    double conventionalCarbon = matchedConventional.get().getCarbonImpact() != null
                            ? matchedConventional.get().getCarbonImpact() : carbon;
                    double savedPerUnit = conventionalCarbon - carbon;
                    if (savedPerUnit > 0) {
                        totalCarbonSaved += savedPerUnit * item.getQuantity();
                    }
                }
            }
        }

        double netCarbon = totalCarbonUsed - totalCarbonSaved;

        Order order = new Order(null, userId, LocalDate.now(), totalCarbonUsed, totalCarbonSaved, netCarbon, totalPrice);
        Order savedOrder = orderRepository.save(order);

        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(savedOrder.getId());
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItemRepository.save(orderItem);
        }

        cartRepository.deleteAll(cartItems);
        return savedOrder;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    private String extractKeyword(String name) {
        if (name == null || name.isBlank()) return "product";

        String cleaned = name.toLowerCase()
                .replaceAll("\\b(eco|organic|friendly|certified|premium|gold|silver|natural|bamboo|bio|pure|green|kg|g|pack|1kg|5kg|10kg|litre|l|ml|gm)\\b", "")
                .replaceAll("[^a-z\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        return Arrays.stream(cleaned.split("\\s+"))
                .filter(w -> w.length() >= 3)
                .max(Comparator.comparingInt(String::length))
                .orElse("product");
    }
}
