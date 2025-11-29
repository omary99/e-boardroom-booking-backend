package com.boardroom.boardroom_booking.DTO;

import com.boardroom.boardroom_booking.EnumData.BookingStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingRequest {
    public String roomName;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public String purpose;
    public BookingStatus status;
    public Long userId;

    public BookingRequest() {
    }

    public BookingRequest(String roomName, LocalDate date, LocalTime startTime, LocalTime endTime, String purpose, Long userId, Long departmentId) {
        this.roomName = roomName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.userId = userId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
