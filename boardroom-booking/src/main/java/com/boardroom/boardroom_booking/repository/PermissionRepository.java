package com.boardroom.boardroom_booking.repository;

import com.boardroom.boardroom_booking.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query("SELECT DISTINCT c FROM Permission c WHERE c.active = true")
    Page<Permission> findAllPermissions(Pageable pageable, String query);

    @Query("SELECT DISTINCT p FROM Permission p WHERE p.uuid = :uuid")
    Optional<Permission> findByUuid(UUID uuid);

    @Query("SELECT DISTINCT p FROM Permission p WHERE p.name = :permissionName")
    Optional<Permission> findByName(String permissionName);

    @Query(value = "SELECT per.*   FROM permissions per INNER JOIN role_permissions rop ON (per.id = rop.permission_id) INNER JOIN roles rol ON (rol.id = rop.role_id) INNER JOIN user_roles ruser " +
            "ON(rol.id = ruser.role_id) INNER JOIN users us ON (us.id = ruser.user_id) WHERE us.id=:userId", nativeQuery = true)
    List<Permission> getUserPermissions(@Param("userId") Long userId);
}
