package com.boardroom.boardroom_booking.Configuration;

import com.boardroom.boardroom_booking.EnumData.UserRole;
import com.boardroom.boardroom_booking.model.Department;
import com.boardroom.boardroom_booking.model.User;
import com.boardroom.boardroom_booking.service.DepartmentService;
import com.boardroom.boardroom_booking.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
        private final UserService userService;
        private final DepartmentService departmentService;
        private final PasswordEncoder passwordEncoder;

        public DataInitializer(UserService userService,
                               DepartmentService departmentService,
                               PasswordEncoder passwordEncoder) {
            this.userService = userService;
            this.departmentService = departmentService;
            this.passwordEncoder = passwordEncoder;
        }

        @Override
        public void run(String... args) throws Exception {
            createDefaultDepartments();
            createDefaultUsers();
        }

        private void createDefaultDepartments() {
            if (departmentService.getDepartmentByName("IT").isEmpty()) {
                Department it = new Department();
                it.setName("IT");
                it.setDescription("IT Department");
                departmentService.createDepartment(it);
            }

            if (departmentService.getDepartmentByName("HR").isEmpty()) {
                Department hr = new Department();
                hr.setName("HR");
                hr.setDescription("Human Resources");
                departmentService.createDepartment(hr);
            }
        }

        private void createDefaultUsers() {
            Department it = departmentService.getDepartmentByName("IT").orElseThrow();
            Department hr = departmentService.getDepartmentByName("HR").orElseThrow();

            // Admin user
            if (userService.getUserByEmail("admin@company.com").isEmpty()) {
                User admin = new User();
                admin.setFullName("System Admin");
                admin.setEmail("admin@company.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(UserRole.ADMIN);
                admin.setDepartment(it);
                admin.setPhoneNumber("0711000000");
                userService.createUser(admin);
                System.out.println("Default admin user created.");
            }

            // HR user
            if (userService.getUserByEmail("hr@company.com").isEmpty()) {
                User hrUser = new User();
                hrUser.setFullName("HR Manager");
                hrUser.setEmail("hr@company.com");
                hrUser.setPassword(passwordEncoder.encode("hr123"));
                hrUser.setRole(UserRole.USER);
                hrUser.setDepartment(hr);
                hrUser.setPhoneNumber("0722000000");
                userService.createUser(hrUser);
                System.out.println("Default HR user created.");
            }
        }
    }


