package com.boardroom.boardroom_booking.model;

import com.boardroom.boardroom_booking.EnumData.BookingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bookings",
        indexes = {@Index(name = "idx_booking_boardroom_date", columnList = "boardroom_id, date"),
                    @Index(name = "idx_booking_date", columnList = "date")})

public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private String purpose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.BOOKED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardroom_id", nullable = false)
    private Boardroom boardroom;

    public Booking() {
    }

    public Booking(LocalDate date, LocalTime startTime, LocalTime endTime, String purpose, BookingStatus status, User user, Department department, Boardroom boardroom) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.status = status;
        this.user = user;
        this.department = department;
        this.boardroom = boardroom;
    }

    public Booking(Long id, LocalDate date, LocalTime startTime, LocalTime endTime, String purpose, BookingStatus status, User user, Department department, Boardroom boardroom) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.status = status;
        this.user = user;
        this.department = department;
        this.boardroom = boardroom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Boardroom getBoardroom() {
        return boardroom;
    }

    public void setBoardroom(Boardroom boardroom) {
        this.boardroom = boardroom;
    }
}
