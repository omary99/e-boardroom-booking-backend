package com.boardroom.boardroom_booking.controller;

import com.boardroom.boardroom_booking.DTO.UpdateUserDto;
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

    private List<UserDto> mapToDto(List<User> users) {
        return users.stream().map(user -> {
            UserDto dto = new UserDto();

            dto.setFirstName(user.getFirstName());
            dto.setMiddleName(user.getMiddleName());
            dto.setSurname(user.getSurname());

            String fullName = user.getFirstName() +
                    (user.getMiddleName() != null ? " " + user.getMiddleName() : "") +
                    " " + user.getSurname();

            dto.setFullName(fullName);

            dto.setPhoneNumber(user.getPhoneNumber());

            dto.setEmail(user.getEmail());
            dto.setGender(user.getGender());

            dto.setActive(user.getActive());

            if (user.getDepartment() != null) {
                dto.setDepartmentId(user.getDepartment().getId());
                dto.setDepartmentName(user.getDepartment().getName());
            }

            return dto;
        }).toList();
    }


    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        return mapToDto(userService.getAllUsers());
    }

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

//    @PutMapping("/{id}")
//    public User updateUser(@PathVariable Long id, @RequestBody User user){
//        return userService.updateUser(id, user);
//    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody UpdateUserDto dto){
        return userService.updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public void  deleteUser(@PathVariable Long id){
         userService.deleteUser(id);
    }

    @PutMapping("/deactivate/{id}")
    public User deactivateUser(@PathVariable Long id) {
        return userService.deactivateUser(id);
    }

    @PutMapping("/activate/{id}")
    public User activateUser(@PathVariable Long id) {
        return userService.activateUser(id);
    }

    @GetMapping
    public List<UserDto> getActiveUsers() {
        return mapToDto(userService.getActiveUsers());
    }

    @GetMapping("/deactivated")
    public List<UserDto> getDeactivatedUsers() {
        return mapToDto(userService.getInactiveUsers());
    }
}
