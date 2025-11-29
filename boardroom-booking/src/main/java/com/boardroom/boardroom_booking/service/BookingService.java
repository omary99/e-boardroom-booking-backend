package com.boardroom.boardroom_booking.service;

import com.boardroom.boardroom_booking.model.Booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking createBooking(Booking booking);

    List<Booking> getAllBookings();
    Optional<Booking> getBookingById(Long id);

    List<Booking> getBookingsByDate(LocalDate date);
    List<Booking> getBookingsByStartTime(LocalTime startTime);
    List<Booking> getBookingsByEndTime(LocalTime endTime);

    long countTomorrowMeetings();

    List<Booking> getBookingsByDepartment(Long departmentId);

    List<Booking> getBookingsByUserId(Long userId);


    Booking updateBooking(Long id, Booking booking);

    void deleteBooking(Long id);


//    Test
void updateStatuses();



}
