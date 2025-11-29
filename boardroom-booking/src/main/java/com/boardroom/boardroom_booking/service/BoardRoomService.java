package com.boardroom.boardroom_booking.service;

import com.boardroom.boardroom_booking.EnumData.BoardroomStatus;
import com.boardroom.boardroom_booking.model.Boardroom;

import java.util.List;
import java.util.Optional;

public interface BoardRoomService {
    Boardroom createBoardroom(Boardroom boardroom);

    Boardroom updateBoardroom(Integer id, Boardroom boardroom);

    Optional<Boardroom> getBoardroomById(Integer id);

    List<Boardroom> getAllBoardrooms();

    void deleteBoardroom(Integer id);

    Boardroom changeStatus(Integer id, BoardroomStatus roomStatus);
}
