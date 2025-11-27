package com.ecobazzar.eco.bazzar.controller;

import com.ecobazzar.eco.bazzar.repository.OrderRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecobazzar.eco.bazzar.dto.UserReport;
import com.ecobazzar.eco.bazzar.model.User;
import com.ecobazzar.eco.bazzar.repository.UserRepository;
import com.ecobazzar.eco.bazzar.service.UserReportService;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@RestController
@RequestMapping("/api/reports")
public class UserReportController {

    private final UserReportService userReportService;

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    public UserReportController(UserReportService userReportService,
                                UserRepository userRepository,
                                OrderRepository orderRepository) {
        this.userReportService = userReportService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public UserReport getUserReport() {
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userReportService.getUserReport(currentUser.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/weekly")
    public List<Map<String, Object>> getWeeklyCarbon() {
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);

        List<Object[]> savedRows = orderRepository.getDailyCarbonSaved(
                user.getId(), Date.valueOf(start), Date.valueOf(today));
        List<Object[]> usedRows = orderRepository.getDailyCarbonUsed(
                user.getId(), Date.valueOf(start), Date.valueOf(today));

        Map<LocalDate, Double> savedMap = new HashMap<>();
        Map<LocalDate, Double> usedMap = new HashMap<>();

        for (Object[] row : savedRows) {
            LocalDate d = ((Date) row[0]).toLocalDate();
            Double val = row[1] == null ? 0.0 : ((Number) row[1]).doubleValue();
            savedMap.put(d, val);
        }
        for (Object[] row : usedRows) {
            LocalDate d = ((Date) row[0]).toLocalDate();
            Double val = row[1] == null ? 0.0 : ((Number) row[1]).doubleValue();
            usedMap.put(d, val);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            Map<String, Object> day = new HashMap<>();
            day.put("day", d.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            day.put("saved", savedMap.getOrDefault(d, 0.0));
            day.put("used", usedMap.getOrDefault(d, 0.0));
            result.add(day);
        }
        return result;
    }
}
