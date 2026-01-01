package com.boardroom.boardroom_booking.controller;

import com.boardroom.boardroom_booking.DTO.BookingRequest;
import com.boardroom.boardroom_booking.DTO.BookingResponseDto;
import com.boardroom.boardroom_booking.model.Boardroom;
import com.boardroom.boardroom_booking.model.Booking;
import com.boardroom.boardroom_booking.model.Department;
import com.boardroom.boardroom_booking.model.User;
import com.boardroom.boardroom_booking.service.BoardRoomService;
import com.boardroom.boardroom_booking.service.BookingService;
import com.boardroom.boardroom_booking.service.DepartmentService;
import com.boardroom.boardroom_booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private BookingResponseDto mapToDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setRoomName(booking.getBoardroom().getRoomName());
        dto.setDate(booking.getDate());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setPurpose(booking.getPurpose() != null ? booking.getPurpose() : "Booked");
        dto.setStatus(booking.getStatus());

        BookingResponseDto.UserDto userDto = new BookingResponseDto.UserDto();
        userDto.setFullName(booking.getUser() != null ? booking.getUser().getFullName() : "N/A");
        dto.setUser(userDto);

        BookingResponseDto.DepartmentDto deptDto = new BookingResponseDto.DepartmentDto();
        deptDto.setName(booking.getDepartment() != null ? booking.getDepartment().getName() : "N/A");
        dto.setDepartment(deptDto);

        return dto;
    }

    private final BookingService bookingService;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final BoardRoomService boardRoomService;

    @Autowired
    public BookingController(BookingService bookingService, UserService userService, DepartmentService departmentService, BoardRoomService boardRoomService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.departmentService = departmentService;
        this.boardRoomService = boardRoomService;
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        bookingService.updateStatuses();

        List<BookingResponseDto> list = bookingService.getAllBookings()
                .stream()
                .map(this::mapToDto)
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable Long id) {
        bookingService.updateStatuses();

        Booking booking = bookingService.getBookingById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return ResponseEntity.ok(mapToDto(booking));
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByUserId(@PathVariable Long userId) {
        List<BookingResponseDto> list = bookingService.getBookingsByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();

        return ResponseEntity.ok(list);
    }


    @GetMapping("/department/{id}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByDepartment(@PathVariable("id") Long departmentId) {
        if (departmentId == null) {
            return ResponseEntity.badRequest().body(null);
        }

        List<BookingResponseDto> list = bookingService.getBookingsByDepartment(departmentId)
                .stream()
                .map(this::mapToDto)
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByRoom(@PathVariable Long roomId){
        List<BookingResponseDto> list = bookingService.getBookingsByRoom(roomId)
                .stream()
                .map(this::mapToDto)
                .toList();

        return ResponseEntity.ok(list);
    }


    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest dto) {

        Boardroom boardroom = boardRoomService.getBoardroomById(dto.getBoardroomId().intValue())
                .orElseThrow(() -> new RuntimeException("Boardroom not found"));

        User user = userService.getUserById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Department department = departmentService.getDepartmentById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Booking booking = new Booking(
                dto.getDate(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getPurpose(),
                dto.getStatus(),
                user,
                department,
                boardroom
        );

        return ResponseEntity.ok(bookingService.createBooking(booking));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateBooking(
            @PathVariable Long id,
            @RequestBody BookingRequest request) {

        Booking existing = bookingService.getBookingById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"));

        User user = userService.getUserById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        Department department = departmentService.getDepartmentById(request.getDepartmentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Department not found"));

        Boardroom boardroom = boardRoomService
                .getBoardroomById(request.getBoardroomId().intValue())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Boardroom not found"));

        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End time must be after start time"
            );
        }

        existing.setBoardroom(boardroom);
        existing.setDate(request.getDate());
        existing.setStartTime(request.getStartTime());
        existing.setEndTime(request.getEndTime());
        existing.setPurpose(request.getPurpose());
        existing.setStatus(request.getStatus());
        existing.setUser(user);
        existing.setDepartment(department);

        bookingService.updateBooking(id, existing);

        return ResponseEntity.ok(Map.of("message", "Booking is updated successfully"));
    }



    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }
}
