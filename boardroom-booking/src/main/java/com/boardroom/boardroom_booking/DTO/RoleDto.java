package com.boardroom.boardroom_booking.DTO;

import lombok.Data;

import java.util.List;

@Data
public class RoleDto {

    private String name;

    private String description;

    private Long roleGroupId;

    private List<Long> permissions;
}
