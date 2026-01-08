package com.pastryshop.pastry_backend.service;

import java.time.LocalDate;
import java.util.Map;

public interface DashboardService {

    Map<String, Object> getTodayStats();

    Map<String, Object> getMonthlyStats();

    Map<String, Object> getSalesChart(LocalDate from, LocalDate to);
}

