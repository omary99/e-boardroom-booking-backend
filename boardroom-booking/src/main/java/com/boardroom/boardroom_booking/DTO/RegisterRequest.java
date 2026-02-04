package com.boardroom.boardroom_booking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String middleName;
    private String surname;
    private String gender;
    private String email;
    private String phoneNumber;
    private String password;
    private Long departmentId;
}
