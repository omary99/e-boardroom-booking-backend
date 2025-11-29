package com.boardroom.boardroom_booking.DTO;

import com.boardroom.boardroom_booking.model.Department;

import java.util.List;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
    private Long departmentId;

    public RegisterRequest() {
    }

    public RegisterRequest(String fullName, String email, String phoneNumber, String password, Long departmentId) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.departmentId = departmentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
