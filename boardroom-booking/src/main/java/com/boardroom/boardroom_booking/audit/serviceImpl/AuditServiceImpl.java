package com.boardroom.boardroom_booking.audit.serviceImpl;

import com.boardroom.boardroom_booking.Configuration.security.CurrentUserUtil;
import com.boardroom.boardroom_booking.audit.dto.AuditDto;
import com.boardroom.boardroom_booking.audit.entity.AuditTrail;
import com.boardroom.boardroom_booking.audit.repository.AuditRepository;
import com.boardroom.boardroom_booking.audit.service.AuditService;
import com.boardroom.boardroom_booking.response.ListResponseWrapper;
import com.boardroom.boardroom_booking.response.ResponseWrapper;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private  final GlobalMethod globalMethod;

    private final AuditRepository auditRepository;

    @Autowired
    private CurrentUserUtil loggedUser;


    @Override
    public ResponseEntity<?> getAudits(Integer pageNo, Integer pageSize, String[] sort, String query) {
        ListResponseWrapper responseWrapper = new ListResponseWrapper();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by(globalMethod.sortByParameter(sort)));
        try {

            Page<AuditTrail> auditTrails = auditRepository.findAll(pageable);

            if (auditTrails.isEmpty()){
                statusCode = ResponseCode.NO_RECORD_FOUND;
                description = "No Audit trails found";
                return GlobalMethod.customResponse(statusCode, description,responseWrapper);
            }
            responseWrapper.setResponse(auditTrails);
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);

        }catch (Exception e){
            log.error("Error in listing audit trails:{} {}",e,"Failed");
            statusCode = ResponseCode.FAILURE;
            description = "Failed: "+e;
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);
        }
    }

    @Override
    public ResponseEntity<?> createAudits(AuditDto auditDto) {
        ResponseWrapper<AuditTrail> responseWrapper = new ResponseWrapper<>();
        Integer statusCode = ResponseCode.SUCCESS;
        String description = "Successfully";
        try {

            AuditTrail auditTrail = new AuditTrail();
            auditTrail.setCreatedBy(String.valueOf(auditDto.getCreatedBy()));
            auditTrail.setUsername(auditDto.getUsername());
            //auditTrail.setInstitutionId(auditDto.getInstitutionId());
            Utility.copyNonNullProperties(auditDto, auditTrail);
            auditRepository.save(auditTrail);
            responseWrapper.setItem(auditTrail);
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);
        }catch (Exception e){
            log.error("Error in saving Applicant:{} {}",e,"Failed");
            statusCode = ResponseCode.FAILURE;
            description = "Failed: "+e;
            return GlobalMethod.customResponse(statusCode, description, responseWrapper);
        }
    }

    @Override
    public void log(String serviceName, String title, String content, String username) {

        //String username = (UserDetails) authentication.getPrincipal();
        //String username = loggedUser.getCurrentUser().getUsername();

        AuditTrail audit = new AuditTrail(serviceName, title, content);
        audit.setUsername(username);

        auditRepository.save(audit);
    }

    private String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return "SYSTEM";
        return auth.getName();
    }
}
