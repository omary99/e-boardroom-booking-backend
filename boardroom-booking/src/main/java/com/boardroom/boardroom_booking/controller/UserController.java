package com.boardroom.boardroom_booking.controller;

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

    @GetMapping
    public List<User> getAllUser(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/email/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping("/department/{department}")
    public Optional<User> getUserByDepartment(@PathVariable String department){
        return userService.getUserByDepartment(department);
    }

    @GetMapping("/department/{id}")
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
