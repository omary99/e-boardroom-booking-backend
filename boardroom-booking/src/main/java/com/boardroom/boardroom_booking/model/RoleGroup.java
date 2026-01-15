package com.boardroom.boardroom_booking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "role_groups")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class RoleGroup {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false)
    private Long id;

    @Basic(optional = false)
    @Column(name = "name", unique = true)
    private String name;

    @Basic(optional = false)
    @Column(name = "Active")
    private Boolean active = true;

    @Basic(optional = false)
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Basic(optional = false)
    @UpdateTimestamp
    @Column(name = "RecordUpdatedDate")
    private LocalDateTime updatedDate;
}
