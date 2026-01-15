package com.boardroom.boardroom_booking.repository;

import com.boardroom.boardroom_booking.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @Query("SELECT DISTINCT c FROM Role c WHERE c.active = true")
    Page<Role> findAllRoles(Pageable pageable, String query);

    @Query("SELECT DISTINCT p FROM Role p WHERE p.uuid = :uuid")
    Optional<Role> findByUuid(UUID uuid);

    @Query(value = "SELECT r.* FROM roles r " +
            "INNER JOIN user_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = :userId",
            nativeQuery = true)
    List<Role> findRolesByUserId(Long id);

    List<Role> findByNameIn(List<String> list);
}

