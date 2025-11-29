package com.boardroom.boardroom_booking.repository;

import com.boardroom.boardroom_booking.model.Boardroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardroomRepository extends JpaRepository<Boardroom, Integer> {
    boolean existsByRoomNameIgnoreCase(String roomName);


}
