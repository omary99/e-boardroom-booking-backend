package com.boardroom.boardroom_booking.service;

import com.boardroom.boardroom_booking.DTO.DashboardResponse;
import com.boardroom.boardroom_booking.repository.BoardroomRepository;
import com.boardroom.boardroom_booking.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {
    private final BookingRepository bookingRepository;
    private final BoardroomRepository boardroomRepository;

    public DashboardServiceImpl(BookingRepository bookingRepository, BoardroomRepository boardroomRepository) {
        this.bookingRepository = bookingRepository;
        this.boardroomRepository = boardroomRepository;
    }

    @Override
    public DashboardResponse getDashboard(Long userId) {
        // Count ongoing meetings
        long ongoing = bookingRepository.countOngoingMeetings();

        // Count tomorrow meetings
        long tomorrow = bookingRepository.countByDateExcludingCancelled(LocalDate.now().plusDays(1));

        // Count my upcoming bookings
        long myUpcoming = bookingRepository.countMyUpcomingBookings(userId);

        // Available boardrooms today
        long totalRooms = boardroomRepository.count();
//        List<String> bookedToday = bookingRepository.findBookedRoomsToday();
//        long available = totalRooms - bookedToday.size();

        long cancelledToday = bookingRepository.countCancelledBookingsToday();

        // Build response
        DashboardResponse response = new DashboardResponse();
        response.setOngoingMeetings(ongoing);
        response.setTomorrowMeetings(tomorrow);
        response.setCancelledBookingsToday(cancelledToday);
        response.setMyUpcomingBookings(myUpcoming);

        return response;
    }
}

