package com.boardroom.boardroom_booking.service;

import com.boardroom.boardroom_booking.DTO.RoleDto;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.UUID;

public interface RoleService {
    ResponseEntity<?> getRoles(Integer pageNo, Integer pageSize, String[] sortBy, String institutionUid, String query, Boolean isApproved);

    ResponseEntity<?> getRoleByUuid(String uuid);

    ResponseEntity<?> saveRole(RoleDto roleDto);

    ResponseEntity<?> editRole(RoleDto roleDto, String uuid);

    ResponseEntity<?> deleteRole(String uuid);

    ResponseEntity<?> getRolesByUser(UUID userUuid);

    ResponseEntity<?> getPermissionsByRole(UUID uuid);

    List<String> getPermissionsByRoleName(String roleName);
}
