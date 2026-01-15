package com.boardroom.boardroom_booking.audit.controller;

import com.boardroom.boardroom_booking.audit.dto.AuditDto;
import com.boardroom.boardroom_booking.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/audit")
public class AuditController {

    private final AuditService auditService;

    //@PreAuthorize("hasAnyRole('ROLE_CAN_VIEW_AUDIT_TRAIL')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAudits(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "createdAt,desc") String[] sortBy, @RequestParam(required = false) String institutionUid, @RequestParam Optional<String> search){
        String query = search.orElse(null);
        return auditService.getAudits(pageNo,pageSize,sortBy,query);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAudit(@RequestBody AuditDto auditDto) throws Exception{
        return auditService.createAudits(auditDto);
    }
}
