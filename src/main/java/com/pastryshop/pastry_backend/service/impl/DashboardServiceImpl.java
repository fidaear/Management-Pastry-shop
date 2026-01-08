package com.pastryshop.pastry_backend.service.impl;

import com.pastryshop.pastry_backend.model.Sale;
import com.pastryshop.pastry_backend.repository.SaleRepository;
import com.pastryshop.pastry_backend.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final SaleRepository saleRepository;

    public DashboardServiceImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public Map<String, Object> getTodayStats() {

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);

        List<Sale> sales = saleRepository.findSalesBetween(start, end);

        double revenue = sales.stream()
                .mapToDouble(Sale::getTotalAmount)
                .sum();

        double benefit = sales.stream()
                .mapToDouble(Sale::getTotalBenefit)
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("salesCount", sales.size());
        stats.put("revenue", revenue);
        stats.put("benefit", benefit);

        return stats;
    }

    @Override
    public Map<String, Object> getMonthlyStats() {

        LocalDateTime start = LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay();

        List<Sale> sales = saleRepository.findSalesFrom(start);

        Map<String, Object> stats = new HashMap<>();
        stats.put("salesCount", sales.size());
        stats.put("revenue",
                sales.stream().mapToDouble(Sale::getTotalAmount).sum());
        stats.put("benefit",
                sales.stream().mapToDouble(Sale::getTotalBenefit).sum());

        return stats;
    }

    @Override
    public Map<String, Object> getSalesChart(LocalDate from, LocalDate to) {

        List<Sale> sales = saleRepository.findSalesBetween(
                from.atStartOfDay(),
                to.atTime(23, 59, 59)
        );

        Map<String, Double> dailyRevenue = new HashMap<>();

        for (Sale sale : sales) {
            String day = sale.getSaleDate().toLocalDate().toString();
            dailyRevenue.put(
                    day,
                    dailyRevenue.getOrDefault(day, 0.0)
                            + sale.getTotalAmount()
            );
        }

        Map<String, Object> chart = new HashMap<>();
        chart.put("dailyRevenue", dailyRevenue);

        return chart;
    }
}

