package com.boardroom.boardroom_booking.DTO;

import com.boardroom.boardroom_booking.EnumData.BoardroomStatus;

import java.time.LocalDateTime;

public class BoardroomResponseDto {
    private Integer id;
    private String roomName;
    private Integer capacity;
    private BoardroomStatus roomStatus;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BoardroomResponseDto() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public BoardroomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(BoardroomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
