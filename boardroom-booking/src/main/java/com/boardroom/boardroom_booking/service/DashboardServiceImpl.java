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

    public DashboardServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public DashboardResponse getDashboard(Long userId) {
        long ongoing = bookingRepository.countOngoingMeetings();
        long tomorrow = bookingRepository.countByDateExcludingCancelled(LocalDate.now().plusDays(1));
        long myUpcoming = bookingRepository.countMyUpcomingBookings(userId);
        long cancelledToday = bookingRepository.countCancelledBookingsToday();

        return new DashboardResponse(ongoing, tomorrow, cancelledToday, myUpcoming);
    }

}

