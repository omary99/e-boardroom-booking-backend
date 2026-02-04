package com.boardroom.boardroom_booking.controller;

import com.boardroom.boardroom_booking.DTO.UserDto;
import com.boardroom.boardroom_booking.model.User;
import com.boardroom.boardroom_booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

//    @GetMapping
//    public List<User> getAllUser(){
//        return userService.getAllUsers();
//    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream().map(user -> {
            UserDto dto = new UserDto();
            dto.setFirstName(user.getFirstName());
            dto.setMiddleName(user.getMiddleName());
            dto.setSurname(user.getSurname());

            String fullName = user.getFirstName() +
                    (user.getMiddleName() != null ? " " + user.getMiddleName() : "") +
                    " " + user.getSurname();
            dto.setFullName(fullName);

            dto.setEmail(user.getEmail());
            dto.setGender(user.getGender());

            if (user.getDepartment() != null) {
                dto.setDepartmentId(user.getDepartment().getId());
                dto.setDepartmentName(user.getDepartment().getName());
            }

            return dto;
        }).toList();
    }


//    @GetMapping("/{id}")
//    public Optional<User> getUserById(@PathVariable Long id){
//        return userService.getUserById(id);
//    }

    @GetMapping("/{id}")
    public Optional<UserDto> getUserById(@PathVariable Long id){
        return userService.getUserById(id).map(user -> {
            UserDto dto = new UserDto();
            dto.setFirstName(user.getFirstName());
            dto.setMiddleName(user.getMiddleName());
            dto.setSurname(user.getSurname());
            dto.setFullName(
                    user.getFirstName() +
                            (user.getMiddleName() != null ? " " + user.getMiddleName() : "") +
                            " " + user.getSurname()
            );
            dto.setEmail(user.getEmail());
            dto.setGender(user.getGender());
            if (user.getDepartment() != null) {
                dto.setDepartmentId(user.getDepartment().getId());
                dto.setDepartmentName(user.getDepartment().getName());
            }
            return dto;
        });
    }

    @GetMapping("/email/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping("/department/{department}")
    public Optional<User> getUserByDepartment(@PathVariable String department){
        return userService.getUserByDepartment(department);
    }

    @GetMapping("/department/id/{id}")
    public List<User> getUserByDepartmentId(@PathVariable Long id){
        return userService.getUsersByDepartmentId(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user){
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void  deleteUser(@PathVariable Long id){
         userService.deleteUser(id);
    }
}
