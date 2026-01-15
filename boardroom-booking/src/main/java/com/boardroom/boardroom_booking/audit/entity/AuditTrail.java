package com.boardroom.boardroom_booking.audit.entity;

import com.boardroom.boardroom_booking.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.io.Serializable;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "audits")
public class AuditTrail extends BaseEntity implements Serializable {

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "content", columnDefinition="TEXT")
    private String content;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "username", columnDefinition = "TEXT")
    private String username;


    public AuditTrail() {

    }

    public AuditTrail(String serviceName,String title, String content) {
        this.serviceName = serviceName;
        this.title=title;
        this.content = content;
    }
}
