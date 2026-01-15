package com.boardroom.boardroom_booking.repository;

import com.boardroom.boardroom_booking.model.RoleGroup;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RoleGroupRepository extends JpaRepository<RoleGroup, Long> {

    public RoleGroup findByName(String name);


    Optional<RoleGroup> findByNameIgnoreCase(String name);
}
