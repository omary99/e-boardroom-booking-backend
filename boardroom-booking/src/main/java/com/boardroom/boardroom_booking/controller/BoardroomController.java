package com.boardroom.boardroom_booking.controller;

import com.boardroom.boardroom_booking.DTO.BoardroomRequestDto;
import com.boardroom.boardroom_booking.DTO.BoardroomResponseDto;
import com.boardroom.boardroom_booking.EnumData.BoardroomStatus;
import com.boardroom.boardroom_booking.model.Boardroom;
import com.boardroom.boardroom_booking.service.BoardRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/boardrooms")
public class BoardroomController {

    private final BoardRoomService boardRoomService;

    public BoardroomController(BoardRoomService boardRoomService) {
        this.boardRoomService = boardRoomService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<BoardroomResponseDto> createBoardroom(@RequestBody BoardroomRequestDto dto) {
        Boardroom room = mapToEntity(dto);
        Boardroom saved = boardRoomService.createBoardroom(room);
        return ResponseEntity.ok(mapToResponseDto(saved));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<BoardroomResponseDto> updateBoardroom(
            @PathVariable Integer id,
            @RequestBody BoardroomRequestDto dto
    ) {
        Boardroom updated = boardRoomService.updateBoardroom(id, mapToEntity(dto));
        return ResponseEntity.ok(mapToResponseDto(updated));
    }

    // GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<BoardroomResponseDto> getBoardroomById(@PathVariable Integer id) {
        Boardroom room = boardRoomService.getBoardroomById(id)
                .orElseThrow(() -> new RuntimeException("Boardroom not found"));

        return ResponseEntity.ok(mapToResponseDto(room));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<BoardroomResponseDto>> getAllBoardrooms() {
        List<BoardroomResponseDto> list = boardRoomService.getAllBoardrooms()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoardroom(@PathVariable Integer id) {
        boardRoomService.deleteBoardroom(id);
        return ResponseEntity.ok("Boardroom deleted successfully");
    }

    // CHANGE STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<BoardroomResponseDto> changeStatus(
            @PathVariable Integer id,
            @RequestParam BoardroomStatus status
    ) {
        Boardroom updated = boardRoomService.changeStatus(id, status);
        return ResponseEntity.ok(mapToResponseDto(updated));
    }

    private Boardroom mapToEntity(BoardroomRequestDto dto) {
        Boardroom room = new Boardroom();
        room.setRoomName(dto.getRoomName());
        room.setCapacity(dto.getCapacity());
        room.setLocation(dto.getLocation());

        if (dto.getRoomStatus() != null) {
            room.setRoomStatus(dto.getRoomStatus());
        }
        return room;
    }

    private BoardroomResponseDto mapToResponseDto(Boardroom boardroom) {
        BoardroomResponseDto dto = new BoardroomResponseDto();
        dto.setId(boardroom.getId());
        dto.setRoomName(boardroom.getRoomName());
        dto.setCapacity(boardroom.getCapacity());
        dto.setRoomStatus(boardroom.getRoomStatus());
        dto.setLocation(boardroom.getLocation());
        dto.setCreatedAt(boardroom.getCreatedAt());
        dto.setUpdatedAt(boardroom.getUpdatedAt());
        return dto;
    }
}

