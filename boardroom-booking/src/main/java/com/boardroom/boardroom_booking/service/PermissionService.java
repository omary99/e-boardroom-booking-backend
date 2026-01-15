package com.boardroom.boardroom_booking.service;

import com.boardroom.boardroom_booking.DTO.PermissionDto;
import org.springframework.http.ResponseEntity;

public interface PermissionService {
    ResponseEntity<?> getPermissions(Integer pageNo, Integer pageSize, String[] sortBy, String institutionUid, String query, Boolean isApproved);

    ResponseEntity<?> getPermissionByUuid(String uuid);

    ResponseEntity<?> savePermission(PermissionDto permissionDto);

    ResponseEntity<?> editPermission(PermissionDto permissionDto, String uuid);

    ResponseEntity<?> deletePermission(String uuid);
}
