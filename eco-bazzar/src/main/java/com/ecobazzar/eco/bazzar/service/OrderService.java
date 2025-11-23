package com.ecobazzar.eco.bazzar.service;

import java.time.LocalDate;
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

    public OrderService(
            CartRepository cartRepository,
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

        double totalPrice = 0;
        double totalCarbonUsed = 0;
        double totalCarbonSaved = 0;

        for (CartItem item : cartItems) {

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            double price = product.getPrice() != null ? product.getPrice() : 0.0;
            double carbon = product.getCarbonImpact() != null ? product.getCarbonImpact() : 0.0;

            totalPrice += price * item.getQuantity();
            totalCarbonUsed += carbon * item.getQuantity();

            if (!Boolean.TRUE.equals(product.getEcoCertified())) {

                Optional<Product> ecoAlt = productRepository
                        .findFirstByEcoCertifiedTrueAndNameContainingIgnoreCase(product.getName());

                if (ecoAlt.isPresent()) {
                    double ecoCarbon = ecoAlt.get().getCarbonImpact() != null ? ecoAlt.get().getCarbonImpact() : 0.0;
                    double saved = (carbon - ecoCarbon) * item.getQuantity();
                    if (saved > 0) totalCarbonSaved += saved;
                }
            }
        }

        double totalCarbon = totalCarbonUsed - totalCarbonSaved;

        Order order = new Order(
                null,
                userId,
                LocalDate.now(),
                totalCarbonUsed,
                totalCarbonSaved,
                totalCarbon,
                totalPrice
        );

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
}
