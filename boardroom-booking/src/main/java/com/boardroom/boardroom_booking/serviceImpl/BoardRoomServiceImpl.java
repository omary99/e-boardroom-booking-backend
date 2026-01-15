package com.boardroom.boardroom_booking.serviceImpl;

import com.boardroom.boardroom_booking.EnumData.BoardroomStatus;
import com.boardroom.boardroom_booking.model.Boardroom;
import com.boardroom.boardroom_booking.repository.BoardroomRepository;
import com.boardroom.boardroom_booking.service.BoardRoomService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BoardRoomServiceImpl implements BoardRoomService {

    private final BoardroomRepository boardroomRepository;

    public BoardRoomServiceImpl(BoardroomRepository boardroomRepository) {
        this.boardroomRepository = boardroomRepository;
    }

    @Override
    public Boardroom createBoardroom(Boardroom boardroom) {
        if (boardroomRepository.existsByRoomNameIgnoreCase(boardroom.getRoomName())) {
            throw new RuntimeException("Room name already exists");
        }

        return boardroomRepository.save(boardroom);
    }

    @Override
    public Boardroom updateBoardroom(Integer id, Boardroom boardroom) {
        Optional<Boardroom> existing = boardroomRepository.findById(id);

        if (existing.isEmpty()) {
            throw new RuntimeException("Boardroom not found with id: " + id);
        }

        Boardroom room = existing.get();
        room.setRoomName(boardroom.getRoomName());
        room.setCapacity(boardroom.getCapacity());
        room.setRoomStatus(boardroom.getRoomStatus());
        room.setLocation(boardroom.getLocation());
        room.setUpdatedAt(boardroom.getUpdatedAt());

        return boardroomRepository.save(room);
    }

    @Override
    public Optional<Boardroom> getBoardroomById(Integer id) {
        return boardroomRepository.findById(id);
    }

    @Override
    public List<Boardroom> getAllBoardrooms() {
        return boardroomRepository.findAll();
    }

    @Override
    public void deleteBoardroom(Integer id) {
        if (!boardroomRepository.existsById(id)) {
            throw new RuntimeException("Boardroom not found with id: " + id);
        }
        boardroomRepository.deleteById(id);
    }

    @Override
    public Boardroom changeStatus(Integer id, BoardroomStatus status) {
        Boardroom room = boardroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Boardroom not found with id: " + id));

        room.setRoomStatus(status);
        return boardroomRepository.save(room);
    }
}
