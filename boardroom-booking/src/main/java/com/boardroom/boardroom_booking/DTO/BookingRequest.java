package com.boardroom.boardroom_booking.DTO;

import com.boardroom.boardroom_booking.EnumData.BookingStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingRequest {
    private Long boardroomId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String purpose;
    private BookingStatus status;
    private Long userId;
    private Long departmentId;

    public BookingRequest() {
    }

    public BookingRequest(Long boardroomId, LocalDate date, LocalTime startTime, LocalTime endTime, String purpose, BookingStatus status, Long userId, Long departmentId) {
        this.boardroomId = boardroomId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.status = status;
        this.userId = userId;
        this.departmentId = departmentId;
    }

    public Long getBoardroomId() {
        return boardroomId;
    }

    public void setBoardroomId(Long boardroomId) {
        this.boardroomId = boardroomId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
