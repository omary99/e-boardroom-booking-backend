package com.boardroom.boardroom_booking.DTO;

import com.boardroom.boardroom_booking.EnumData.BoardroomStatus;

public class BoardroomRequestDto {
    private String roomName;
    private Integer capacity;
    private BoardroomStatus roomStatus;
    private String location;

    public BoardroomRequestDto() {}

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
}
