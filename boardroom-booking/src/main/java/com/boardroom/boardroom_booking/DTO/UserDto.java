package com.boardroom.boardroom_booking.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private String name;
    private String firstName;
    private String middleName;
    private String surname;
    private String fullName;
    private String gender;
    private String email;
    private String password;
    private String confirmPassword;
    private String mobileNumber;
    private String signature;
    private List<Long> roles;


    private Long departmentId;
    private String departmentName;
}
