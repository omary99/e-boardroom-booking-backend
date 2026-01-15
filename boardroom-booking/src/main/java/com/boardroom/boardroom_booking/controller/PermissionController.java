package com.boardroom.boardroom_booking.controller;

import com.boardroom.boardroom_booking.DTO.PermissionDto;
import com.boardroom.boardroom_booking.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/permission")
public class PermissionController {

    private final PermissionService permissionService;

    @PreAuthorize("hasAnyRole('CAN_MANAGE_PERMISSIONS')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPermissions(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "createdAt,desc") String[] sortBy, @RequestParam(required = false) String institutionUid, @RequestParam Optional<String> search, @RequestParam(defaultValue = "true") Boolean isApproved){
        String query = search.orElse(null);
        return permissionService.getPermissions(pageNo,pageSize,sortBy,institutionUid,query,isApproved);
    }

    @PreAuthorize("hasAnyRole('CAN_MANAGE_PERMISSIONS')")
    @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> getPermission(@PathVariable String uuid){
        return permissionService.getPermissionByUuid(uuid);
    }

    @PreAuthorize("hasAnyRole('CAN_MANAGE_PERMISSIONS')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> savePermission(@RequestBody PermissionDto permissionDto){
        return permissionService.savePermission(permissionDto);
    }

    @PreAuthorize("hasAnyRole('CAN_MANAGE_PERMISSIONS')")
    @PutMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editPermission(@RequestBody PermissionDto permissionDto, @PathVariable("uuid") String uuid){
        return permissionService.editPermission(permissionDto,uuid);
    }

    @PreAuthorize("hasAnyRole('CAN_MANAGE_PERMISSIONS')")
    @DeleteMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePermission(@PathVariable("uuid") String uuid){
        return permissionService.deletePermission(uuid);
    }
}
