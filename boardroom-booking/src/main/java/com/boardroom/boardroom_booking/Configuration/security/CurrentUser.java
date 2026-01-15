package com.boardroom.boardroom_booking.Configuration.security;

import lombok.Data;

@Data
public class CurrentUser {
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String roles;
    private Long currentLocation;
}
