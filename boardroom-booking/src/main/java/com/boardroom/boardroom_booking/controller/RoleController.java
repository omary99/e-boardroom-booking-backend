package com.boardroom.boardroom_booking.controller;

import com.boardroom.boardroom_booking.DTO.RoleDto;
import com.boardroom.boardroom_booking.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/role")
public class RoleController {

    private final RoleService roleService;

    //@PreAuthorize("hasAnyRole('CAN_MANAGE_ROLES')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRoles(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "createdAt,desc") String[] sortBy, @RequestParam(required = false) String institutionUid, @RequestParam Optional<String> search, @RequestParam(defaultValue = "true") Boolean isApproved){
        String query = search.orElse(null);
        return roleService.getRoles(pageNo,pageSize,sortBy,institutionUid,query,isApproved);
    }

    //@PreAuthorize("hasAnyRole('CAN_MANAGE_ROLES')")
    @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> getRole(@PathVariable String uuid){
        return roleService.getRoleByUuid(uuid);
    }

    //@PreAuthorize("hasAnyRole('CAN_MANAGE_ROLES')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveRole(@RequestBody RoleDto roleDto){
        return roleService.saveRole(roleDto);
    }

    //@PreAuthorize("hasAnyRole('CAN_MANAGE_ROLES')")
    @PutMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editRole(@RequestBody RoleDto roleDto, @PathVariable("uuid") String uuid){
        return roleService.editRole(roleDto,uuid);
    }

    //@PreAuthorize("hasAnyRole('CAN_MANAGE_ROLES')")
    @DeleteMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteRole(@PathVariable("uuid") String uuid){
        return roleService.deleteRole(uuid);
    }

    //@PreAuthorize("hasAnyRole('CAN_MANAGE_ROLES')")
    @GetMapping(value="/role/user/{userUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRolesByUser(@PathVariable("userUuid") UUID userUuid){
        return roleService.getRolesByUser(userUuid);
    }


    //@PreAuthorize("hasAnyRole('CAN_MANAGE_ROLES')")
    @GetMapping(value="/role/{uuid}/permission", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPermissionsByRole(@PathVariable("uuid") UUID uuid){
        return roleService.getPermissionsByRole(uuid);
    }

    //@PreAuthorize("hasAnyRole('CAN_MANAGE_ROLES')")
    @GetMapping(value="/{roleName}/permission", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getPermissionsByRole(@PathVariable("roleName") String roleName){
        return roleService.getPermissionsByRoleName(roleName);
    }
}
