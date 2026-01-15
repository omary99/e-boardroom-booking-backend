package com.boardroom.boardroom_booking.repository;

import com.boardroom.boardroom_booking.model.Department;
import com.boardroom.boardroom_booking.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    Optional<User> findByDepartmentName(String name);
    boolean existsByEmail(String email);
    List<User> findByDepartmentId(Long departmentId);

    @Query(value = "SELECT * FROM users u WHERE u.uuid = :uuid", nativeQuery = true)
    Optional<User> findFirstByUuid(UUID uuid);

    @Query(value = "SELECT u FROM User u WHERE u.active = true AND (COALESCE(:query, '') = '' OR CAST(u.username AS text) LIKE CONCAT('%', CAST(:query AS text), '%'))")
    Page<User> findAllUsers(String query, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(String username);
}
