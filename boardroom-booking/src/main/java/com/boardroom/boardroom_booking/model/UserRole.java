package com.boardroom.boardroom_booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "user_roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRole extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-One: many roles can belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // Many-to-One: each user role entry is linked to a single role
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
