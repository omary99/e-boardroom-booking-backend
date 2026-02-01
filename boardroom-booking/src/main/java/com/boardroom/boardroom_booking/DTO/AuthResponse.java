package com.boardroom.boardroom_booking.DTO;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String tokenType = "Bearer";
    private long expiresIn;
    private String username;
    private String roles;
    private long userId;

    public AuthResponse(String token,String refreshToken,String tokenType ,long expiresIn, String username, String roles,long userId) {
        this.token = token;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.username = username;
        this.roles = roles;
        this.userId = userId;
    }

}
