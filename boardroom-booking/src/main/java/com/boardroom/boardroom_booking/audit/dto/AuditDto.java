package com.boardroom.boardroom_booking.audit.dto;

import lombok.Data;

@Data
public class AuditDto {

    private String serviceName;

    private String content;

    private String title;

    private Long createdBy;

    private Long institutionId;

    private String username;
}
