package com.boardroom.boardroom_booking.DTO;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private String email;
    // getters and setters
}
