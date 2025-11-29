package com.boardroom.boardroom_booking.controller;

import com.boardroom.boardroom_booking.DTO.BookingRequest;
import com.boardroom.boardroom_booking.model.Booking;
import com.boardroom.boardroom_booking.model.Department;
import com.boardroom.boardroom_booking.model.User;
import com.boardroom.boardroom_booking.service.BookingService;
import com.boardroom.boardroom_booking.service.DepartmentService;
import com.boardroom.boardroom_booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;
    private final DepartmentService departmentService;

    @Autowired
    public BookingController(BookingService bookingService, UserService userService, DepartmentService departmentService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.departmentService = departmentService;
    }


    @GetMapping
    public List<Booking> getAllBookings() {
        bookingService.updateStatuses();
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public Optional<Booking> getBookingById(@PathVariable Long id) {
        bookingService.updateStatuses();
        return bookingService.getBookingById(id);
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getBookingsByUserId(@PathVariable Long userId) {
        return bookingService.getBookingsByUserId(userId);
    }


    @GetMapping("/department/{id}")
    public ResponseEntity<List<Booking>> getBookingsByDepartment(@PathVariable("id") Long departmentId) {
        if (departmentId == null) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            List<Booking> bookings = bookingService.getBookingsByDepartment(departmentId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }


    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        System.out.println("Received BookingRequest: " + request.userId );

        User user = userService.getUserById(request.userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Department department = user.getDepartment();

        System.out.println("Received department: " + department );


        Booking booking = new Booking(
                request.roomName,
                request.date,
                request.startTime,
                request.endTime,
                request.purpose,
                request.status,
                user,
                department
        );

        Booking saved = bookingService.createBooking(booking);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Long id,
            @RequestBody BookingRequest request) {

        Booking existing = bookingService.getBookingById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        User user = userService.getUserById(request.userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Department department = user.getDepartment();

        existing.setRoomName(request.roomName);
        existing.setDate(request.date);
        existing.setStartTime(request.startTime);
        existing.setEndTime(request.endTime);
        existing.setPurpose(request.purpose);
        existing.setStatus(request.status);
        existing.setUser(user);
        existing.setDepartment(department);

        Booking updated = bookingService.updateBooking(id, existing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }
}
