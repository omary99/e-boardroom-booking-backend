package com.boardroom.boardroom_booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role_permissions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
