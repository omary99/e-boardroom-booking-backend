package com.boardroom.boardroom_booking.DTO;


import lombok.Data;

@Data
public class UpdateUserDto {
    private String firstName;
    private String middleName;
    private String surname;
    private String email;
    private String phoneNumber;
    private String gender;
    private Long departmentId;
}
