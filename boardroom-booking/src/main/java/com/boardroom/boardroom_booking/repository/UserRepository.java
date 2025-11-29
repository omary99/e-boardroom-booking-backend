package com.boardroom.boardroom_booking.repository;

import com.boardroom.boardroom_booking.model.Department;
import com.boardroom.boardroom_booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    Optional<User> findByDepartmentName(String name);
    boolean existsByEmail(String email);
    List<User> findByDepartmentId(Long departmentId);

}
