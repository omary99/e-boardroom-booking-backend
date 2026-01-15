package com.boardroom.boardroom_booking.serviceImpl;

import com.boardroom.boardroom_booking.Configuration.security.CurrentUserUtil;
import com.boardroom.boardroom_booking.DTO.RoleDto;
import com.boardroom.boardroom_booking.audit.service.AuditService;
import com.boardroom.boardroom_booking.model.Permission;
import com.boardroom.boardroom_booking.model.Role;
import com.boardroom.boardroom_booking.model.RoleGroup;
import com.boardroom.boardroom_booking.model.User;
import com.boardroom.boardroom_booking.repository.*;
import com.boardroom.boardroom_booking.response.ListResponseWrapper;
import com.boardroom.boardroom_booking.response.ResponseWrapper;
import com.boardroom.boardroom_booking.service.RoleService;
import com.boardroom.boardroom_booking.utils.GlobalMethod;
import com.boardroom.boardroom_booking.utils.ResponseCode;
import com.boardroom.boardroom_booking.utils.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private  final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleGroupRepository roleGroupRepository;
    private final AuditService auditService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    private final GlobalMethod globalMethod;

    @Override
    public ResponseEntity<?> getRoles(Integer pageNo, Integer pageSize, String[] sortBy, String institutionUid, String query, Boolean isApproved) {
        ListResponseWrapper responseWrapper = new ListResponseWrapper();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by(globalMethod.sortByParameter(sortBy)));
        try {
            Page<Role> roles = roleRepository.findAllRoles(pageable,query);


            if (roles.isEmpty()){
                statusCode = ResponseCode.NO_RECORD_FOUND;
                description = "No Organization unit found";
                return GlobalMethod.customResponse(statusCode, description,responseWrapper);
            }
            responseWrapper.setResponse(roles);
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);

        }catch (Exception e){
            log.error("Error in listing organization :{} {}",e,"Failed");
            statusCode = ResponseCode.FAILURE;
            description = "Failed: "+e;
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);
        }
    }

    @Override
    public ResponseEntity<?> getRoleByUuid(String uuid) {
        ResponseWrapper<Role> responseWrapper = new ResponseWrapper<>();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";

        try {
            Optional<Role> role = roleRepository.findByUuid(UUID.fromString(uuid));
            if (role.isEmpty()){
                statusCode = ResponseCode.NO_RECORD_FOUND;
                description = "No Organization level with the defined id";
                return GlobalMethod.customResponse(statusCode, description,responseWrapper);
            }
            responseWrapper.setItem(role.get());
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);

        }catch (Exception e){
            log.error("Error in getting charge rate :{} {}",e,"Failed");
            statusCode = ResponseCode.FAILURE;
            description = "Failed: "+e;
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);
        }
    }

    @Override
    public ResponseEntity<?> saveRole(RoleDto roleDto) {
        ResponseWrapper<Role> responseWrapper = new ResponseWrapper<>();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";
        try {
//            String response =  Utility.checkNullFields(roleDto);
//            if(!response.equals("true")){
//                statusCode = ResponseCode.DATA_MISSED_IN_PAYLOAD;
//                description = response;
//                return GlobalMethod.customResponse(statusCode,description, responseWrapper);
//            }

            Role role = new Role();

            List<Permission> permissions = new ArrayList<>();
            Utility.copyNonNullProperties(roleDto, role);
            roleDto.getPermissions().forEach(perm -> {
                Optional<Permission> optPerm = permissionRepository.findById(perm);
                optPerm.ifPresent(permissions::add);
            });
            role.setPermissions(permissions);
            role.setCreatedBy(String.valueOf(currentUserUtil.getCurrentUser().getUserId()));

            Role savedRole = roleRepository.save(role);
            responseWrapper.setItem(savedRole);
            auditService.log("USER-MANAGEMENT-SERVICE","Create Role","User "+currentUserUtil.getCurrentUser().getUsername()+" created role "+savedRole.getName(),currentUserUtil.getCurrentUser().getUsername());
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);
        }catch (Exception e){
            log.error("Error in saving Organization level:{} {}",e,"Failed");
            statusCode = ResponseCode.FAILURE;
            description = "Failed: "+e;
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);
        }
    }

    @Override
    public ResponseEntity<?> editRole(RoleDto roleDto, String uuid) {
        ResponseWrapper<Role> responseWrapper = new ResponseWrapper<>();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";

        try {
            Optional<Role> role = roleRepository.findByUuid(UUID.fromString(uuid));
            if(role.isEmpty()){
                statusCode = ResponseCode.DATA_MISSED_IN_PAYLOAD;
                description = "No Organization level with the given id!";
                return GlobalMethod.customResponse(statusCode, description, responseWrapper);
            }

            //Utils.updateFields(unit.get(),unitDto);
            Utility.copyNonNullProperties(roleDto,role.get());

            List<Permission> permissions = new ArrayList<>();
            if (roleDto.getPermissions() != null && !roleDto.getPermissions().isEmpty()) {
                roleDto.getPermissions().forEach(perm -> {
                    Optional<Permission> optPerm = permissionRepository.findById(perm);
                    optPerm.ifPresent(permissions::add);
                });
                role.get().setPermissions(permissions);
            }

            if (roleDto.getRoleGroupId() != null) {
                Optional<RoleGroup> optRoleGroup = roleGroupRepository.findById(roleDto.getRoleGroupId());
                if (optRoleGroup.isEmpty()) {
                    statusCode = ResponseCode.DATA_MISSED_IN_PAYLOAD;
                    description = "This role group not found!";
                    return globalMethod.response(statusCode, description, responseWrapper);
                }
                //newRole.setRoleGroup(optRoleGroup.get());
            }
            role.get().setUpdatedAt(LocalDateTime.now());
            role.get().setUpdatedBy(String.valueOf(currentUserUtil.getCurrentUser().getUserId()));
            Role savedRole = roleRepository.save(role.get());

            responseWrapper.setItem(savedRole);
            auditService.log("USER-MANAGEMENT-SERVICE","Edit Role","User "+currentUserUtil.getCurrentUser().getUsername()+" edited role "+role.get().getName(),currentUserUtil.getCurrentUser().getUsername());
            return globalMethod.response(statusCode,description,responseWrapper);

        }
        catch (Exception e){
            description = "Failed: "+e;
            statusCode = ResponseCode.FAILURE;
            return globalMethod.response(statusCode,description,responseWrapper);
        }
    }

    @Override
    public ResponseEntity<?> deleteRole(String uuid) {
        ResponseWrapper<Role> responseWrapper = new ResponseWrapper<>();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";
        try {
            Optional<Role> role =  roleRepository.findByUuid(UUID.fromString(uuid));
            if(role.isEmpty()){
                statusCode = ResponseCode.DATA_MISSED_IN_PAYLOAD;
                description = "No organizational level found with the given id!";
                return GlobalMethod.customResponse(statusCode, description, responseWrapper);
            }
            Role roleToDelete  = role.get();
            roleToDelete.setActive(false);
            role.get().setUpdatedBy(String.valueOf(currentUserUtil.getCurrentUser().getUserId()));
            roleRepository.save(roleToDelete);

            auditService.log("USER-MANAGEMENT-SERVICE","Delete Role","User "+currentUserUtil.getCurrentUser().getUsername()+" Deactivated Role",currentUserUtil.getCurrentUser().getUsername());
            return globalMethod.response(statusCode,description,responseWrapper);

        }
        catch (Exception e){
            description = "Failed: "+e;
            statusCode = ResponseCode.FAILURE;
            return globalMethod.response(statusCode,description,responseWrapper);
        }
    }

    @Override
    public ResponseEntity<?> getRolesByUser(UUID userUuid) {
        ListResponseWrapper responseWrapper = new ListResponseWrapper();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";
        ObjectMapper mapper = new ObjectMapper();
        try {

            Optional<User> optUser = userRepository.findFirstByUuid(userUuid);
            if (optUser.isEmpty())
                return globalMethod.response(ResponseCode.NO_RECORD_FOUND, "Sorry, this user does not exist!", responseWrapper);

            User user = optUser.get();
            log.info(":::::::: [Role]:  Getting Roles by User: {} with id: {} ::::::::", user.getUsername(), user.getId());

            List<Role> roleList = roleRepository.findRolesByUserId(user.getId());
            responseWrapper.setItemList(roleList);

            return globalMethod.response(statusCode, description, responseWrapper);

        } catch (Exception e) {
            log.error("Error getting Roles by User with id: {} : %%%%%%%%%5  {} %%%%%%%%%%%%%%%%", userUuid, e);
            statusCode = ResponseCode.FAILURE;
            description = "Failure!";
            return globalMethod.response(statusCode, description, responseWrapper);
        }
    }

    @Override
    public ResponseEntity<?> getPermissionsByRole(UUID uuid) {
        ListResponseWrapper responseWrapper = new ListResponseWrapper();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";

        Optional<Role> optionalRole = roleRepository.findByUuid(uuid);
        if (optionalRole.isEmpty()) {
            statusCode = ResponseCode.NO_RECORD_FOUND;
            description = "No Roles found";
            return globalMethod.response(statusCode, description, responseWrapper);
        }
        responseWrapper.setItemList(optionalRole.get().getPermissions().stream().toList());
        return globalMethod.response(statusCode, description, responseWrapper);
    }

//    @Override
//    public List<String> getPermissionsByRoleName(String roleName) {
//        List<String> permissions = new ArrayList<>();
//        Optional<Role> role = roleRepository.findByName(roleName);
//        if (role.isEmpty()) {
//            permissions.add("No Role found");
//            return permissions;
//        }
//        List<Permission> permissionList = rolePermissionRepository.findByRole(role.get());
//        if(permissionList.isEmpty()){
//            permissions.add("No permissions found");
//            return permissions;
//        }
//        for (Permission permission : permissionList){
//            permissions.add(permission.getName());
//        }
//        return permissions;
//    }

    @Override
    public List<String> getPermissionsByRoleName(String roleName) {
        List<String> permissions = new ArrayList<>();

        // Split role names if comma separated
        String[] roleNames = roleName.split(",");

        // Fetch all roles that match any of the given names
        List<Role> roles = roleRepository.findByNameIn(Arrays.asList(roleNames));

        if (roles.isEmpty()) {
            permissions.add("No Role found");
            return permissions;
        }

        // Collect all permissions for these roles
        List<Permission> permissionList = rolePermissionRepository.findPermissionsByRoleIn(roles);

        if (permissionList.isEmpty()) {
            permissions.add("No permissions found");
            return permissions;
        }

        // Add permission names to result (avoiding duplicates)
        for (Permission permission : permissionList) {
            if (!permissions.contains(permission.getName())) {
                permissions.add(permission.getName());
            }
        }

        return permissions;
    }

}
