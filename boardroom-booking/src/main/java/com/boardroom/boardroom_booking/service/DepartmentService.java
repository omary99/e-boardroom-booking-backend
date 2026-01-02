package com.boardroom.boardroom_booking.service;

import com.boardroom.boardroom_booking.model.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Department createDepartment(Department department);

    List<Department> getAllDepartments();

    Optional<Department> getDepartmentById(Long id);

    Department updateDepartment(Long id, Department department);

    void deleteDepartment(Long id);

    Optional<Department> getDepartmentByName(String name);

}
