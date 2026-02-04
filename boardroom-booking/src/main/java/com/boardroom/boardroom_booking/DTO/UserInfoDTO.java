package com.boardroom.boardroom_booking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class UserInfoDTO {
    private Long id;
    private String fullName;
    private String email;
    private Long departmentId;
    private String department;
    private List<String> roles;
}
