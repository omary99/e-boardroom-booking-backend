package com.boardroom.boardroom_booking.repository;

import com.boardroom.boardroom_booking.model.Permission;
import com.boardroom.boardroom_booking.model.Role;
import com.boardroom.boardroom_booking.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    @Query("SELECT DISTINCT rp.permission FROM RolePermission rp WHERE rp.role = :role")
    List<Permission> findByRole(Role role);

    @Query("SELECT rp.permission FROM RolePermission rp WHERE rp.role IN :roles")
    List<Permission> findPermissionsByRoleIn(@Param("roles") List<Role> roles);
}
