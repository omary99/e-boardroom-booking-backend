package com.boardroom.boardroom_booking.audit.service;

import com.boardroom.boardroom_booking.audit.dto.AuditDto;
import org.springframework.http.ResponseEntity;


public interface AuditService {
    ResponseEntity<?> getAudits(Integer pageNo, Integer pageSize, String[] sortBy, String query);

    ResponseEntity<?> createAudits(AuditDto auditDto);

    void log(String serviceName, String title, String content,String username);
}
