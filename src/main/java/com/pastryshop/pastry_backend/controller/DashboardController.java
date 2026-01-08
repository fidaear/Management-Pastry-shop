package com.pastryshop.pastry_backend.controller;

import com.pastryshop.pastry_backend.service.DashboardService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/today")
    public Map<String, Object> today() {
        return dashboardService.getTodayStats();
    }

    @GetMapping("/month")
    public Map<String, Object> month() {
        return dashboardService.getMonthlyStats();
    }

    @GetMapping("/chart")
    public Map<String, Object> chart(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        return dashboardService.getSalesChart(from, to);
    }
}

