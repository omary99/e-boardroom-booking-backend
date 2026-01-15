package com.boardroom.boardroom_booking.controller;

import com.boardroom.boardroom_booking.DTO.RoleGroupDto;
import com.boardroom.boardroom_booking.service.RoleGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RoleGroupController {

    @Autowired
    private RoleGroupService roleGroupService;

    @PreAuthorize("hasAnyRole('CAN_MANAGE_ROLES')")
    @PostMapping(value="/role-group")
    public ResponseEntity<?> saveRoleGroup(@RequestBody RoleGroupDto roleGroupDto) {
        return  roleGroupService.saveRoleGroup(roleGroupDto);
    }

    @PreAuthorize("hasAnyRole('CAN_MANAGE_ROLES')")
    @PutMapping(value="/role-group/{id}")
    public ResponseEntity<?> editRoleGroup(@RequestBody RoleGroupDto roleGroupDto, @PathVariable("id") Long id) {
        return  roleGroupService.editRoleGroup(roleGroupDto,id);
    }

    @PreAuthorize("hasAnyRole('CAN_MANAGE_ROLES')")
    @DeleteMapping(value="/role-group/{id}")
    public ResponseEntity<?> deleteRoleGroup(@PathVariable("id") Long id) {
        return  roleGroupService.deleteRoleGroup(id);
    }

    @PreAuthorize("hasAnyRole('CAN_MANAGE_ROLES')")
    @GetMapping(value="/role-group", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRoleGroup(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "createdAt, desc") String[] sortBy){
        return roleGroupService.getRoleGroup(pageNo, pageSize,sortBy);
    }
}
