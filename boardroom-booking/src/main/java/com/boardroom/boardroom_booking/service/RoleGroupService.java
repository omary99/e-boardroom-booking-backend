package com.boardroom.boardroom_booking.service;

import com.boardroom.boardroom_booking.DTO.RoleGroupDto;
import org.springframework.http.ResponseEntity;


public interface RoleGroupService {
    ResponseEntity<?> saveRoleGroup(RoleGroupDto roleGroupDto);

    ResponseEntity<?> editRoleGroup(RoleGroupDto roleGroupDto, Long id);

    ResponseEntity<?> deleteRoleGroup(Long id);

    ResponseEntity<?> getRoleGroup(Integer pageNo, Integer pageSize, String[] sortBy);
}
