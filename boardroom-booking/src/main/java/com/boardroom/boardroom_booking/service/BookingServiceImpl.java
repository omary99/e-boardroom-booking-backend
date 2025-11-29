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
        boolean conflictExist = bookingRepository.existsByRoomNameAndDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                booking.getRoomName(),
                booking.getDate(),
                booking.getStartTime(),
                booking.getEndTime()
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
    public List<Booking> getBookingsByDate(LocalDate date) {
        return bookingRepository.findBookingByDate(date);
    }

    @Override
    public List<Booking> getBookingsByStartTime(LocalTime startTime) {
        return bookingRepository.findBookingByStartTime(startTime);
    }

    @Override
    public List<Booking> getBookingsByEndTime(LocalTime endTime) {
        return bookingRepository.findBookingByEndTime(endTime);
    }

    @Override
    public long countTomorrowMeetings() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return bookingRepository.countTomorrowMeetings(tomorrow);
    }

    @Override
    public Booking updateBooking(Long id, Booking booking) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found by id: " + id));

        existingBooking.setDate(booking.getDate());
        existingBooking.setDepartment(booking.getDepartment());
        existingBooking.setPurpose(booking.getPurpose());
        existingBooking.setRoomName(booking.getRoomName());
        existingBooking.setStartTime(booking.getStartTime());
        existingBooking.setEndTime(booking.getEndTime());
        existingBooking.setUser(booking.getUser());
        existingBooking.setStatus(booking.getStatus());
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
