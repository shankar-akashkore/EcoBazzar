package com.ecobazzar.eco.bazzar.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.ecobazzar.eco.bazzar.model.Order;
import com.ecobazzar.eco.bazzar.model.Product;
import com.ecobazzar.eco.bazzar.model.User;
import com.ecobazzar.eco.bazzar.repository.OrderRepository;
import com.ecobazzar.eco.bazzar.repository.ProductRepository;
import com.ecobazzar.eco.bazzar.repository.UserRepository;

@Service
public class AdminService {

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    public AdminService(ProductRepository productRepository, UserRepository userRepository, OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Product approveProduct(Long id) {
        return productRepository.findById(id)
                .map(p -> {
                    p.setEcoCertified(true);   // <= ADMIN APPROVES
                    p.setEcoRequested(false);  // <= clear request
                    return productRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public User approveSeller(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole("ROLE_SELLER");
        user.setSellerRequestPending(false);
        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Map<String, Object> getAdminReport(){
        List<Order> orders = orderRepository.findAll();

//        double totalCarbonUsed = orders.stream().mapToDouble(Order::getCarbonUsed).sum();
//        double totalCarbonSaved = orders.stream().mapToDouble(Order::getCarbonSaved).sum();
//        double totalRevenue = orders.stream().mapToDouble(Order::getTotalPrice).sum();


        double totalCarbonUsed = orders.stream()
                .mapToDouble(o -> o.getCarbonUsed() != null ? o.getCarbonUsed() : 0.0)
                .sum();

        double totalCarbonSaved = orders.stream()
                .mapToDouble(o -> o.getCarbonSaved() != null ? o.getCarbonSaved() : 0.0)
                .sum();

        double totalRevenue = orders.stream()
                .mapToDouble(o -> o.getTotalPrice() != null ? o.getTotalPrice() : 0.0)
                .sum();

        Map<String, Object> report = new HashMap<>();
        report.put("totalOrders", orders.size());
        report.put("totalRevenue", totalRevenue);
        report.put("totalCarbonUsed", totalCarbonUsed);
        report.put("totalCarbonSaved", totalCarbonSaved);
        report.put("netCarbon", totalCarbonUsed - totalCarbonSaved);
        report.put("totalUsers", userRepository.count());
        report.put("totalProducts", productRepository.count());

        return report;
    }

    public String generateReportCSV() {
        List<Order> orders = orderRepository.findAll();

        StringBuilder csv = new StringBuilder("OrderId,UserId,TotalPrice,CarbonUsed,CarbonSaved,OrderDate\n");

        for (Order o : orders) {
            csv.append(o.getId()).append(",")
                    .append(o.getUserId()).append(",")
                    .append(o.getTotalPrice()).append(",")
                    .append(o.getCarbonUsed()).append(",")
                    .append(o.getCarbonSaved()).append(",")
                    .append(o.getOrderDate()).append("\n");
        }
        return csv.toString();
    }

    public Product rejectProduct(Long id) {
        return productRepository.findById(id)
                .map(p -> {
                    p.setEcoRequested(false);
                    p.setEcoCertified(false);
                    return productRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
}
