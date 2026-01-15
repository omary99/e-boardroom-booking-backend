package com.boardroom.boardroom_booking.repository;

import com.boardroom.boardroom_booking.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
