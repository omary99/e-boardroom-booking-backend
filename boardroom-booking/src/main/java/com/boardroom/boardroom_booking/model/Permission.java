package com.boardroom.boardroom_booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "permissions")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

//    @ManyToMany(mappedBy = "permissions")
//    @JsonIgnore
//    private Set<Role> roles = new HashSet<>();

    public Permission(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
