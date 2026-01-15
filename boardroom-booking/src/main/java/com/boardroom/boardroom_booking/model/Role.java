package com.boardroom.boardroom_booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseEntity implements Serializable {

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissions = new ArrayList<>();

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private Set<UserRole> userRoles = new HashSet<>();



}