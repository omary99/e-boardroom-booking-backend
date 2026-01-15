package com.boardroom.boardroom_booking.serviceImpl;

import com.boardroom.boardroom_booking.DTO.RoleGroupDto;
import com.boardroom.boardroom_booking.model.RoleGroup;
import com.boardroom.boardroom_booking.repository.RoleGroupRepository;
import com.boardroom.boardroom_booking.response.ListResponseWrapper;
import com.boardroom.boardroom_booking.response.ResponseWrapper;
import com.boardroom.boardroom_booking.service.RoleGroupService;
import com.boardroom.boardroom_booking.utils.GlobalMethod;
import com.boardroom.boardroom_booking.utils.ResponseCode;
import com.boardroom.boardroom_booking.utils.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleGroupServiceImpl implements RoleGroupService {

    private final RoleGroupRepository roleGroupRepository;

    private final GlobalMethod globalMethod;

    @Override
    public ResponseEntity<?> saveRoleGroup(RoleGroupDto roleGroupDto) {
        ResponseWrapper<RoleGroup> responseWrapper = new ResponseWrapper<>();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (roleGroupDto.getName() == null){
                statusCode = ResponseCode.DATA_MISSED_IN_PAYLOAD;
                description = "Name not provided";
                return GlobalMethod.customResponse(statusCode, description, responseWrapper);
            }

            Optional<RoleGroup> optRoleGroup = roleGroupRepository.findByNameIgnoreCase(roleGroupDto.getName());
            if (optRoleGroup.isPresent()){
                statusCode = ResponseCode.DATA_MISSED_IN_PAYLOAD;
                description = "This role group name already exists!";
                return GlobalMethod.customResponse(statusCode, description, responseWrapper);
            }

            RoleGroup newRoleGroup = new RoleGroup();
            Utility.copyNonNullProperties(roleGroupDto,newRoleGroup);
            RoleGroup savedRoleGroup = roleGroupRepository.save(newRoleGroup);
            //Check category of User Registered
            responseWrapper.setItem(savedRoleGroup);
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);

        }catch (Exception e){
            log.error( "Error in saving Role : %%%%%%%%%5  {} %%%%%%%%%%%%%%%%",e);
            statusCode = ResponseCode.FAILURE;
            description = "Failure!";
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);
        }
    }

    @Override
    public ResponseEntity<?> editRoleGroup(RoleGroupDto roleGroupDto, Long id) {
        ResponseWrapper<RoleGroup> responseWrapper = new ResponseWrapper<>();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";
        ObjectMapper mapper = new ObjectMapper();
        try {
            Optional<RoleGroup> optionalRoleGroup = roleGroupRepository.findById(id);
            if (optionalRoleGroup.isEmpty()){
                statusCode = ResponseCode.NO_RECORD_FOUND;
                description = "This role group not found!";
                return GlobalMethod.customResponse(statusCode, description, responseWrapper);
            }

            Optional<RoleGroup> optRoleGroup = roleGroupRepository.findByNameIgnoreCase(roleGroupDto.getName());
            if (optRoleGroup.isPresent() && !Objects.equals(optRoleGroup.get().getId(), id)){
                statusCode = ResponseCode.DATA_MISSED_IN_PAYLOAD;
                description = "This role group name already exists!";
                return GlobalMethod.customResponse(statusCode, description, responseWrapper);
            }

            RoleGroup newRoleGroup = optionalRoleGroup.get();
            Utility.copyNonNullProperties(roleGroupDto,newRoleGroup);
            RoleGroup savedRoleGroup = roleGroupRepository.save(newRoleGroup);
            responseWrapper.setItem(savedRoleGroup);
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);

        }catch (Exception e){
            log.error( "Error in saving Role group : %%%%%%%%%5  {} %%%%%%%%%%%%%%%%",e);
            statusCode = ResponseCode.FAILURE;
            description = "Failure!";
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);
        }
    }

    @Override
    public ResponseEntity<?> deleteRoleGroup(Long id) {
        ResponseWrapper<RoleGroup> responseWrapper = new ResponseWrapper<>();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";
        ObjectMapper mapper = new ObjectMapper();
        try {
            Optional<RoleGroup> optionalRoleGroup = roleGroupRepository.findById(id);
            if (optionalRoleGroup.isEmpty()){
                statusCode = ResponseCode.NO_RECORD_FOUND;
                description = "This role group not found!";
                return globalMethod.customResponse(statusCode, description, responseWrapper);
            }

            RoleGroup newRoleGroup = optionalRoleGroup.get();
            roleGroupRepository.delete(newRoleGroup);
            return globalMethod.customResponse(statusCode, description, responseWrapper);

        }catch (Exception e){
            log.error( "Error in saving Role group : %%%%%%%%%5  {} %%%%%%%%%%%%%%%%",e);
            statusCode = ResponseCode.FAILURE;
            description = "Failure!";
            return globalMethod.customResponse(statusCode, description, responseWrapper);
        }
    }

    @Override
    public ResponseEntity<?> getRoleGroup(Integer pageNo, Integer pageSize, String[] sort) {
        ListResponseWrapper responseWrapper = new ListResponseWrapper();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(globalMethod.sortByParameter(sort)));
        try {


            Page<RoleGroup> roleGroups =roleGroupRepository.findAll(pageable);
            if (roleGroups.isEmpty()){
                statusCode = ResponseCode.NO_RECORD_FOUND;
                description ="No Role Group found";
                return globalMethod.customResponse(statusCode,description,responseWrapper);
            }
            responseWrapper.setResponse(roleGroups);
            return globalMethod.customResponse(statusCode, description,responseWrapper);

        }catch (Exception e){
            log.error("Error in listing Roles Group : {} {} ",e,"Failed");
            statusCode = ResponseCode.FAILURE;
            description = "Failed";
            return globalMethod.customResponse(statusCode,description,responseWrapper);
        }
    }
}
