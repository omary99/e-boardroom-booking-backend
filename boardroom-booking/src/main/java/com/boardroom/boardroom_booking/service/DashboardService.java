package com.boardroom.boardroom_booking.service;

import com.boardroom.boardroom_booking.DTO.DashboardResponse;

public interface DashboardService {
    DashboardResponse getDashboard(Long userId);
}
