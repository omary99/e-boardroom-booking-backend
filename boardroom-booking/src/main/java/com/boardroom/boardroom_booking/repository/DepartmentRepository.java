package com.boardroom.boardroom_booking.repository;

import com.boardroom.boardroom_booking.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
