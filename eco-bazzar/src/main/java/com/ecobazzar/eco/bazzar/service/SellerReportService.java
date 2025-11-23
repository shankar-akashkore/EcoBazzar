package com.ecobazzar.eco.bazzar.service;

import java.util.List;
import java.util.*;
import org.springframework.stereotype.Service;
import com.ecobazzar.eco.bazzar.dto.SellerReport;
import com.ecobazzar.eco.bazzar.model.Product;
import com.ecobazzar.eco.bazzar.model.User;
import com.ecobazzar.eco.bazzar.repository.OrderItemRepository;
import com.ecobazzar.eco.bazzar.repository.ProductRepository;
import com.ecobazzar.eco.bazzar.repository.UserRepository;

@Service
public class SellerReportService {

    private final OrderItemRepository orderItemRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    public SellerReportService(OrderItemRepository orderItemRepository,
                               UserRepository userRepository,
                               ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public SellerReport getSellerReport(String email) {
        User seller = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller Not Found"));
        Long sellerId = seller.getId();
        List<Product> sellerProducts = productRepository.findBySeller_Id(sellerId);  // FIXED
        long totalProducts = sellerProducts.size();
        long totalEcoCertified = sellerProducts.stream()
                .filter(p -> Boolean.TRUE.equals(p.getEcoCertified()))
                .count();
        Double totalRevenue = orderItemRepository.getTotalRevenueBySeller(sellerId);
        if (totalRevenue == null) totalRevenue = 0.0;
        Long totalOrders = (long) orderItemRepository.findBySellerId(sellerId).size();
        String badge = getSellerBadge(totalRevenue, totalEcoCertified);
        return new SellerReport(
                sellerId,
                seller.getName(),
                totalProducts,
                totalEcoCertified,
                totalOrders,
                totalRevenue,
                badge
        );
    }

    public List<Map<String, Object>> getSellerSales(String email, int days) {
        User seller = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Seller Not Found"));
        Long sellerId = seller.getId();

        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate sinceLocal = today.minusDays(days - 1);
        java.sql.Date sinceDate = java.sql.Date.valueOf(sinceLocal);

        List<Object[]> rows = orderItemRepository.getDailyRevenueBySellerSince(sellerId, sinceDate);

        Map<String, Double> revMap = new HashMap<>();
        if (rows != null) {
            for (Object[] r : rows) {
                String day;
                if (r[0] instanceof java.sql.Date) {
                    day = ((java.sql.Date) r[0]).toLocalDate().toString();
                } else {
                    day = String.valueOf(r[0]).substring(0, 10);
                }
                Double revenue = r[1] == null ? 0.0 : ((Number) r[1]).doubleValue();
                revMap.put(day, revenue);
            }
        }

        List<Map<String, Object>> out = new ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            java.time.LocalDate d = today.minusDays(i);
            String dayStr = d.toString();
            Map<String, Object> m = new HashMap<>();
            m.put("day", dayStr);
            m.put("revenue", revMap.getOrDefault(dayStr, 0.0));
            out.add(m);
        }
        return out;
    }

    private String getSellerBadge(Double revenue, Long totalEcoCertified) {
        if (revenue > 100000.0 && totalEcoCertified > 20) return "🏆 Top Seller";
        if (revenue > 50000.0) return "🚀 Growing Seller";
        if (totalEcoCertified > 10) return "🌿 Trusted Eco Seller";
        return "📈 New Seller";
    }
}
