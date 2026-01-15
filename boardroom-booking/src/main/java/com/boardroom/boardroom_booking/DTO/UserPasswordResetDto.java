package com.boardroom.boardroom_booking.DTO;

import lombok.Data;

@Data
public class UserPasswordResetDto {

    private String previousPassword;

    private Long userId;

    private String newPassword;

    private String confirmPassword;
}
