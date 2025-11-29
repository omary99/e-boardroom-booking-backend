package com.boardroom.boardroom_booking.model;

import com.boardroom.boardroom_booking.EnumData.BoardroomStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "boardrooms")
public class Boardroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_name", nullable = false, length = 100, unique = true)
    private String roomName;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardroomStatus roomStatus = BoardroomStatus.ACTIVE;

    @Column(length = 150)
    private String location;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boardroom() {
    }

    public Boardroom(String roomName, Integer capacity, BoardroomStatus roomStatus, String location, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.roomName = roomName;
        this.capacity = capacity;
        this.roomStatus = roomStatus;
        this.location = location;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Boardroom(Integer id, String roomName, Integer capacity, BoardroomStatus roomStatus, String location, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.roomName = roomName;
        this.capacity = capacity;
        this.roomStatus = roomStatus;
        this.location = location;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    @Override
    public String toString() {
        return "BoardRoom{" +
                "id=" + id +
                ", roomName='" + roomName + '\'' +
                ", capacity=" + capacity +
                ", roomStatus=" + roomStatus +
                ", location='" + location + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
