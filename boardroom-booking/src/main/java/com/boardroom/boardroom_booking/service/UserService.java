package com.boardroom.boardroom_booking.service;

import com.boardroom.boardroom_booking.DTO.UpdateUserDto;
import com.boardroom.boardroom_booking.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByDepartment(String name);

    List<User> getUsersByDepartmentId(Long id);

    Optional<User> getUserByEmail(String email);

//    User updateUser(Long id, User user);

    User updateUser(Long id, UpdateUserDto dto);

    void deleteUser(Long id);

    User deactivateUser(Long id);

    User activateUser(Long id);

    List<User> getActiveUsers();

    List<User> getInactiveUsers();

    UserDetails findByUsername(String name);

}
