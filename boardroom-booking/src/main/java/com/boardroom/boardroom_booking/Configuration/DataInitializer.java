package com.boardroom.boardroom_booking.Configuration;

import com.boardroom.boardroom_booking.EnumData.UserRole;
import com.boardroom.boardroom_booking.model.Department;
import com.boardroom.boardroom_booking.model.Permission;
import com.boardroom.boardroom_booking.model.Role;
import com.boardroom.boardroom_booking.model.User;
import com.boardroom.boardroom_booking.repository.PermissionRepository;
import com.boardroom.boardroom_booking.repository.RolePermissionRepository;
import com.boardroom.boardroom_booking.repository.RoleRepository;
import com.boardroom.boardroom_booking.service.DepartmentService;
import com.boardroom.boardroom_booking.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
        private final UserService userService;
        private final DepartmentService departmentService;
        private final PasswordEncoder passwordEncoder;

        private final RoleRepository roleRepository;
        private final PermissionRepository permissionRepository;
        private final RolePermissionRepository rolePermissionRepository;

        public DataInitializer(UserService userService,
                               DepartmentService departmentService,
                               PasswordEncoder passwordEncoder,
                               RoleRepository roleRepository,
                               PermissionRepository permissionRepository,
                               RolePermissionRepository rolePermissionRepository) {
            this.userService = userService;
            this.departmentService = departmentService;
            this.passwordEncoder = passwordEncoder;
            this.roleRepository = roleRepository;
            this.permissionRepository = permissionRepository;
            this.rolePermissionRepository =rolePermissionRepository;
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

            // 1. Ensure ROLE_ADMIN exists
            Role role = new Role();
            role.setName("ROLE_ADMIN");

            System.out.println("=======> 2");
            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(role));



            Role role2 = new Role();
            role2.setName("ROLE_USER");
            Role roleUser = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(role2));

            System.out.println("=======> 3");
            // 2. Feed and ensure permissions exist
            Map<String, List<String>> permissionsMap = Map.ofEntries(
                    Map.entry("USER", List.of("CREATE", "EDIT", "DELETE", "VIEW"))
//                Map.entry("PRODUCTION_PLAN", List.of("CREATE", "EDIT", "VIEW")),
//                Map.entry("PRODUCTION_PLAN_APPROVAL", List.of("UNIT", "PROGRAM", "CENTER")),
//                Map.entry("EXECUTION_PLAN", List.of("CREATE", "EDIT", "VIEW","APPROVE")),
//                Map.entry("ACTIVITY", List.of("CREATE", "EDIT", "DELETE")),
//                Map.entry("ACTIVITY_APPROVAL", List.of("CENTER", "HQ")),
//                Map.entry("BUDGET", List.of("ALLOCATE"))
            );
            System.out.println("=======> 4");

            List<Permission> existingPermissions = rolePermissionRepository.findByRole(roleAdmin);

            System.out.println("=======> 5");
            Set<Permission> newPermissions = new HashSet<>();

            for (Map.Entry<String, List<String>> entry : permissionsMap.entrySet()) {
                String entity = entry.getKey();
                for (String action : entry.getValue()) {
                    String permissionName = action + "_" + entity;
                    Permission permission = permissionRepository.findByName(permissionName)
                            .orElseGet(() -> permissionRepository.save(
                                    new Permission(permissionName, "Allows " + action + " on " + entity)
                            ));
                    if (!existingPermissions.contains(permission)) {
                        newPermissions.add(permission);
                    }
                }
            }
            System.out.println("=======> 6");

            if (!newPermissions.isEmpty()) {
                existingPermissions.addAll(newPermissions);
                roleAdmin.setPermissions(existingPermissions);
                roleRepository.save(roleAdmin);
            }
            System.out.println("=======> 7");


            // Admin user
            if (userService.getUserByEmail("admin@company.com").isEmpty()) {
                User admin = new User();
                admin.setFullName("System Admin");
                admin.setEmail("admin@company.com");
                admin.setUsername("admin@company.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRolez(Set.of(roleAdmin));
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
                hrUser.setUsername("hr@company.com");
                hrUser.setPassword(passwordEncoder.encode("hr123"));
                hrUser.setRolez(Set.of(roleUser));
                hrUser.setDepartment(hr);
                hrUser.setPhoneNumber("0722000000");
                userService.createUser(hrUser);
                System.out.println("Default HR user created.");
            }
        }
    }


