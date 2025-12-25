package com.boardroom.boardroom_booking.service;

import com.boardroom.boardroom_booking.EnumData.BookingStatus;
import com.boardroom.boardroom_booking.Exception.BookingConflictException;
import com.boardroom.boardroom_booking.model.Booking;
import com.boardroom.boardroom_booking.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking createBooking(Booking booking) {
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        LocalDate maxDate = today.plusMonths(1);

        // 1. Check for past date
        if (booking.getDate().isBefore(today)) {
            throw new IllegalArgumentException("Cannot book past dates");
        }

        if (booking.getDate().isAfter(maxDate)) {
            throw new IllegalArgumentException("Cannot book more than 1 month in advance");
        }
        // 2. Check for pastTime if booking is for today
        if (booking.getDate().isEqual(today) &&
                !booking.getStartTime().isAfter(nowTime.plusMinutes(10))) { // optional buffer
            throw new IllegalArgumentException("Booking must be at least 10 minutes in the future");
        }

        // 3. Existing validation
        if (!booking.getEndTime().isAfter(booking.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        boolean conflictExist =
                bookingRepository.existsByBoardroom_IdAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
                        booking.getBoardroom().getId(),
                        booking.getDate(),
                        booking.getEndTime(),
                        booking.getStartTime()
                );

        if (conflictExist) {
            throw new BookingConflictException("This room is already booked for the selected time.");
        }

        return bookingRepository.save(booking);
    }


    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<Booking> getBookingsByDepartment(Long departmentId) {
        return bookingRepository.findByDepartmentId(departmentId);
    }

    @Override
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> getBookingsByRoom(Long roomId) {
        return bookingRepository.findByBoardroom_Id(roomId);
    }

    @Override
    public Booking updateBooking(Long id, Booking bookingRequest) {

        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Booking not found by id: " + id));

        if (!bookingRequest.getEndTime().isAfter(bookingRequest.getStartTime())) {
            throw new IllegalArgumentException(
                    "End time must be after start time");
        }

        if (bookingRequest.getBoardroom() != null) {
            existingBooking.setBoardroom(
                    bookingRequest.getBoardroom()
            );
        }

        existingBooking.setDate(bookingRequest.getDate());
        existingBooking.setStartTime(bookingRequest.getStartTime());
        existingBooking.setEndTime(bookingRequest.getEndTime());
        existingBooking.setPurpose(bookingRequest.getPurpose());
        existingBooking.setStatus(bookingRequest.getStatus());

        boolean conflictExist =
                bookingRepository.existsByBoardroom_IdAndDateAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(
                        existingBooking.getBoardroom().getId(),
                        existingBooking.getDate(),
                        existingBooking.getEndTime(),
                        existingBooking.getStartTime(),
                        existingBooking.getId()
                );

        if (conflictExist) {
            throw new BookingConflictException(
                    "This room is already booked for the selected time."
            );
        }

        return bookingRepository.save(existingBooking);
    }


    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found by id: " + id);
        }
        bookingRepository.deleteById(id);
    }

    public void updateStatuses() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Booking> bookings = bookingRepository.findAll();

        for (Booking booking : bookings) {
            if (booking.getStatus() != BookingStatus.CANCELLED) {

                // Meeting ONGOING
                if (booking.getDate().equals(today)
                        && !now.isBefore(booking.getStartTime())
                        && !now.isAfter(booking.getEndTime())) {

                    booking.setStatus(BookingStatus.ONGOING);
                }

                // Meeting COMPLETED
                else if (booking.getDate().equals(today)
                        && now.isAfter(booking.getEndTime())) {

                    booking.setStatus(BookingStatus.COMPLETED);
                }
            }
        }

        bookingRepository.saveAll(bookings);
    }

}
