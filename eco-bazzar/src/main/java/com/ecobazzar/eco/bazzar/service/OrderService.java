package com.ecobazzar.eco.bazzar.service;

import java.time.LocalDate;
import java.util.*;

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

        System.out.println("\n================== 🧾 CHECKOUT DEBUG START ==================\n");

        for (CartItem item : cartItems) {
            Product current = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            int qty = item.getQuantity();
            double usedImpact = current.getCarbonImpact() != null ? current.getCarbonImpact() : 0.0;
            double price = current.getPrice() != null ? current.getPrice() : 0.0;

            totalPrice += price * qty;
            totalCarbonUsed += usedImpact * qty;

            System.out.println("🛒 Product: " + current.getName());
            System.out.println("   ├─ Quantity: " + qty);
            System.out.println("   ├─ Eco Certified: " + current.getEcoCertified());
            System.out.println("   ├─ Carbon Impact: " + usedImpact + " kg/unit");

            String keyword = extractKeyword(current.getName());
            System.out.println("   ├─ Keyword: " + keyword);

            List<Product> allProducts = productRepository.findAll();

            double normalVersionImpact = usedImpact;
            Product matchedNonEco = null;

            for (Product other : allProducts) {
                if (Objects.equals(other.getId(), current.getId())) continue;
                if (other.getCarbonImpact() == null) continue;

                String otherName = other.getName() != null ? other.getName().toLowerCase() : "";
                if (otherName.contains(keyword) && !Boolean.TRUE.equals(other.getEcoCertified())) {
                    if (other.getCarbonImpact() > normalVersionImpact) {
                        normalVersionImpact = other.getCarbonImpact();
                        matchedNonEco = other;
                    }
                }
            }

            if (matchedNonEco != null && normalVersionImpact > usedImpact) {
                double savedPerUnit = normalVersionImpact - usedImpact;
                double totalSavedForItem = savedPerUnit * qty;
                totalCarbonSaved += totalSavedForItem;

                System.out.println("   ✅ Found higher carbon alternative:");
                System.out.println("      ├─ Normal Version: " + matchedNonEco.getName());
                System.out.println("      ├─ Normal Version Impact: " + normalVersionImpact + " kg/unit");
                System.out.println("      ├─ Saved per unit: " + savedPerUnit + " kg");
                System.out.println("      ├─ Quantity: " + qty);
                System.out.println("      └─ Total Saved for this item: " + totalSavedForItem + " kg");
            } else {
                System.out.println("   ⚠️ No higher-carbon alternative found for comparison.");
            }

            System.out.println("-------------------------------------------------------------");
        }

        double netCarbon = totalCarbonUsed - totalCarbonSaved;

        System.out.println("\n================== 🌍 CHECKOUT SUMMARY ==================\n");
        System.out.println("🧮 Total Carbon Used : " + totalCarbonUsed + " kg");
        System.out.println("💚 Total Carbon Saved: " + totalCarbonSaved + " kg");
        System.out.println("🌡️  Net Carbon Footprint: " + netCarbon + " kg");
        System.out.println("💰 Total Price: ₹" + totalPrice);
        System.out.println("==========================================================\n");

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

        System.out.println("✅ Order successfully saved for user ID: " + userId);
        System.out.println("================== ✅ CHECKOUT DEBUG END ==================\n");

        return savedOrder;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // 🧠 SMARTER keyword extractor
    private String extractKeyword(String name) {
        if (name == null || name.isBlank()) return "";

        name = name.toLowerCase();
        name = name.replaceAll("[^a-z\\s]", " ");
        List<String> words = new ArrayList<>(Arrays.asList(name.split("\\s+")));

        // Remove filler words
        words.removeIf(w -> List.of("eco", "friendly", "organic", "natural", "bio",
                "premium", "certified", "green", "kg", "g", "pack", "litre", "l", "ml", "pure", "best").contains(w));

        if (words.isEmpty()) return "";

        // Prefer last meaningful word, but fallback to middle if last is too generic
        String keyword = words.get(words.size() - 1);
        if (keyword.length() <= 2 && words.size() > 1) {
            keyword = words.get(words.size() - 2);
        }

        return keyword.trim();
    }
}
