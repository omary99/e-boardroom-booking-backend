package com.boardroom.boardroom_booking.serviceImpl;

import com.boardroom.boardroom_booking.Configuration.security.CurrentUserUtil;
import com.boardroom.boardroom_booking.DTO.PermissionDto;
import com.boardroom.boardroom_booking.audit.service.AuditService;
import com.boardroom.boardroom_booking.model.Permission;
import com.boardroom.boardroom_booking.repository.PermissionRepository;
import com.boardroom.boardroom_booking.response.ListResponseWrapper;
import com.boardroom.boardroom_booking.response.ResponseWrapper;
import com.boardroom.boardroom_booking.service.PermissionService;
import com.boardroom.boardroom_booking.utils.GlobalMethod;
import com.boardroom.boardroom_booking.utils.ResponseCode;
import com.boardroom.boardroom_booking.utils.Utility;
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
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private  final GlobalMethod globalMethod;
    private  final PermissionRepository permissionRepository;
    private final AuditService auditService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Override
    public ResponseEntity<?> getPermissions(Integer pageNo, Integer pageSize, String[] sortBy, String institutionUid, String query, Boolean isApproved) {
        ListResponseWrapper responseWrapper = new ListResponseWrapper();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by(globalMethod.sortByParameter(sortBy)));
        try {
            Page<Permission> permissions = permissionRepository.findAllPermissions(pageable,query);


            if (permissions.isEmpty()){
                statusCode = ResponseCode.NO_RECORD_FOUND;
                description = "No Organization unit found";
                return GlobalMethod.customResponse(statusCode, description,responseWrapper);
            }
            responseWrapper.setResponse(permissions);
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);

        }catch (Exception e){
            log.error("Error in listing organization :{} {}",e,"Failed");
            statusCode = ResponseCode.FAILURE;
            description = "Failed: "+e;
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);
        }
    }

    @Override
    public ResponseEntity<?> getPermissionByUuid(String uuid) {
        ResponseWrapper<Permission> responseWrapper = new ResponseWrapper<>();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";

        try {
            Optional<Permission> permission = permissionRepository.findByUuid(UUID.fromString(uuid));
            if (permission.isEmpty()){
                statusCode = ResponseCode.NO_RECORD_FOUND;
                description = "No Organization level with the defined id";
                return GlobalMethod.customResponse(statusCode, description,responseWrapper);
            }
            responseWrapper.setItem(permission.get());
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);

        }catch (Exception e){
            log.error("Error in getting charge rate :{} {}",e,"Failed");
            statusCode = ResponseCode.FAILURE;
            description = "Failed: "+e;
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);
        }
    }

    @Override
    public ResponseEntity<?> savePermission(PermissionDto permissionDto) {
        ResponseWrapper<Permission> responseWrapper = new ResponseWrapper<>();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";
        try {
            String response =  Utility.checkNullFields(permissionDto);
            if(!response.equals("true")){
                statusCode = ResponseCode.DATA_MISSED_IN_PAYLOAD;
                description = response;
                return GlobalMethod.customResponse(statusCode,description, responseWrapper);
            }

            Permission permission = new Permission();

            Utility.copyNonNullProperties(permissionDto, permission);
            permission.setCreatedBy(String.valueOf(currentUserUtil.getCurrentUser().getUserId()));

            Permission savedPermission = permissionRepository.save(permission);
            responseWrapper.setItem(savedPermission);
            auditService.log("USER-MANAGEMENT-SERVICE","Create Permission","User "+currentUserUtil.getCurrentUser().getUsername()+" created permission "+savedPermission.getName(),currentUserUtil.getCurrentUser().getUsername());
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);
        }catch (Exception e){
            log.error("Error in saving Organization level:{} {}",e,"Failed");
            statusCode = ResponseCode.FAILURE;
            description = "Failed: "+e;
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);
        }
    }

    @Override
    public ResponseEntity<?> editPermission(PermissionDto permissionDto, String uuid) {
        ResponseWrapper<Permission> responseWrapper = new ResponseWrapper<>();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";

        try {
            Optional<Permission> permission = permissionRepository.findByUuid(UUID.fromString(uuid));
            if(permission.isEmpty()){
                statusCode = ResponseCode.DATA_MISSED_IN_PAYLOAD;
                description = "No Organization level with the given id!";
                return GlobalMethod.customResponse(statusCode, description, responseWrapper);
            }

            //Utils.updateFields(unit.get(),unitDto);
            Utility.copyNonNullProperties(permissionDto,permission.get());
            permission.get().setUpdatedAt(LocalDateTime.now());
            permission.get().setUpdatedBy(String.valueOf(currentUserUtil.getCurrentUser().getUserId()));
            Permission savedPermission = permissionRepository.save(permission.get());
            auditService.log("USER-MANAGEMENT-SERVICE","Edit Permission","User "+currentUserUtil.getCurrentUser().getUsername()+" edited permission "+savedPermission.getName(),currentUserUtil.getCurrentUser().getUsername());
            responseWrapper.setItem(savedPermission);
            return globalMethod.response(statusCode,description,responseWrapper);

        }
        catch (Exception e){
            description = "Failed: "+e;
            statusCode = ResponseCode.FAILURE;
            return globalMethod.response(statusCode,description,responseWrapper);
        }
    }

    @Override
    public ResponseEntity<?> deletePermission(String uuid) {
        ResponseWrapper<Permission> responseWrapper = new ResponseWrapper<>();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";
        try {
            Optional<Permission> permission =  permissionRepository.findByUuid(UUID.fromString(uuid));
            if(permission.isEmpty()){
                statusCode = ResponseCode.DATA_MISSED_IN_PAYLOAD;
                description = "No organizational level found with the given id!";
                return GlobalMethod.customResponse(statusCode, description, responseWrapper);
            }
            Permission permissionToDelete  = permission.get();
            permissionToDelete.setActive(false);
            permissionRepository.save(permissionToDelete);
            auditService.log("USER-MANAGEMENT-SERVICE","Edit Permission","User "+currentUserUtil.getCurrentUser().getUsername()+" deactivated permission "+permissionToDelete.getName(),currentUserUtil.getCurrentUser().getUsername());
            return globalMethod.response(statusCode,description,responseWrapper);

        }
        catch (Exception e){
            description = "Failed: "+e;
            statusCode = ResponseCode.FAILURE;
            return globalMethod.response(statusCode,description,responseWrapper);
        }
    }
}
