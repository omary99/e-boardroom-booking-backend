//package com.boardroom.boardroom_booking.utils;
//
//import com.boardroom.boardroom_booking.model.Permission;
//import com.boardroom.boardroom_booking.model.Role;
//import com.boardroom.boardroom_booking.model.User;
//import com.boardroom.boardroom_booking.repository.PermissionRepository;
//import com.boardroom.boardroom_booking.repository.RolePermissionRepository;
//import com.boardroom.boardroom_booking.repository.RoleRepository;
//import com.boardroom.boardroom_booking.repository.UserRepository;
//import jakarta.annotation.PostConstruct;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//@Component
//public class DataInitializer {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final RoleRepository roleRepository;
////    private final OrganizationLevelRepository organizationLevelRepository;
////    private  final OrganizationUnitRepository organizationUnitRepository;
//    private final PermissionRepository permissionRepository;
//    private final RolePermissionRepository rolePermissionRepository;
//
//    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.roleRepository = roleRepository;
////        this.organizationLevelRepository = organizationLevelRepository;
////        this.organizationUnitRepository = organizationUnitRepository;
//        this.permissionRepository = permissionRepository;
//        this.rolePermissionRepository =rolePermissionRepository;
//
//    }
//
////    @PostConstruct
////    public void init() {
////        if(userRepository.findByUsername("admin").isEmpty()){
////            Role role = new Role();
////            role.setName("ROLE_ADMIN");
////            //Role roleAdmin =roleRepository.save(role);
////
////            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
////                    .orElseGet(() -> roleRepository.save(role));
////
////            // 2. Feed permissions
////            Map<String, List<String>> permissionsMap = Map.ofEntries(
////                    Map.entry("USER", List.of("CREATE", "EDIT", "DELETE", "VIEW")),
////                    Map.entry("PRODUCTION_PLAN", List.of("CREATE", "EDIT", "VIEW")),
////                    Map.entry("PRODUCTION_PLAN_APPROVAL", List.of("UNIT", "PROGRAM", "CENTER"))
////            );
////
////            Set<Permission> allPermissions = new HashSet<>();
////
////            for (Map.Entry<String, List<String>> entry : permissionsMap.entrySet()) {
////                String entity = entry.getKey();
////                for (String action : entry.getValue()) {
////                    String permissionName = action + "_" + entity;
////                    Permission permission = permissionRepository.findByName(permissionName)
////                            .orElseGet(() -> permissionRepository.save(
////                                    new Permission(permissionName, "Allows " + action + " on " + entity)
////                            ));
////                    allPermissions.add(permission);
////                }
////            }
////
////            // 3. Assign all permissions to admin role
////            roleAdmin.setPermissions(allPermissions);
////            roleRepository.save(roleAdmin);
////
////
////            User user = new User();
////            user.setUsername("admin");
////            user.setPassword(passwordEncoder.encode("Admin@123"));
////            user.setActive(true);
////            user.setEmail("abc@gmail.com");
////            user.setRoles(Set.of(roleAdmin));
////            User savedUser = userRepository.save(user);
////
////            if(organizationLevelRepository.count() < 1){
////                OrganizationLevel organizationLevel = new OrganizationLevel();
////                organizationLevel.setName("HEAD QUARTER");
////                organizationLevel.setDescription("HEAD QUARTER");
////                OrganizationLevel savedOrganizationLevel = organizationLevelRepository.save(organizationLevel);
////
////                OrganizationUnit organizationUnit = new OrganizationUnit();
////                organizationUnit.setName("HEAD QUARTER");
////                organizationUnit.setDescription("HEAD QUARTER");
////                organizationUnit.setOrganizationLevel(savedOrganizationLevel);
////                OrganizationUnit savedOrganizationUnit = organizationUnitRepository.save(organizationUnit);
////
////                savedUser.setCurrentLocation(savedOrganizationUnit);
////                userRepository.save(savedUser);
////
////            }
////        }
////
////
////
////    }
//
//    @PostConstruct
//    public void init() {
//        System.out.println("=======> 1");
//        // 1. Ensure ROLE_ADMIN exists
//        Role role = new Role();
//        role.setName("ROLE_ADMIN");
//
//        System.out.println("=======> 2");
//        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
//                .orElseGet(() -> roleRepository.save(role));
//
//        System.out.println("=======> 3");
//        // 2. Feed and ensure permissions exist
//        Map<String, List<String>> permissionsMap = Map.ofEntries(
//                Map.entry("USER", List.of("CREATE", "EDIT", "DELETE", "VIEW"))
////                Map.entry("PRODUCTION_PLAN", List.of("CREATE", "EDIT", "VIEW")),
////                Map.entry("PRODUCTION_PLAN_APPROVAL", List.of("UNIT", "PROGRAM", "CENTER")),
////                Map.entry("EXECUTION_PLAN", List.of("CREATE", "EDIT", "VIEW","APPROVE")),
////                Map.entry("ACTIVITY", List.of("CREATE", "EDIT", "DELETE")),
////                Map.entry("ACTIVITY_APPROVAL", List.of("CENTER", "HQ")),
////                Map.entry("BUDGET", List.of("ALLOCATE"))
//                );
//        System.out.println("=======> 4");
//
//        List<Permission> existingPermissions = rolePermissionRepository.findByRole(roleAdmin);
//
//        System.out.println("=======> 5");
//        Set<Permission> newPermissions = new HashSet<>();
//
//        for (Map.Entry<String, List<String>> entry : permissionsMap.entrySet()) {
//            String entity = entry.getKey();
//            for (String action : entry.getValue()) {
//                String permissionName = action + "_" + entity;
//                Permission permission = permissionRepository.findByName(permissionName)
//                        .orElseGet(() -> permissionRepository.save(
//                                new Permission(permissionName, "Allows " + action + " on " + entity)
//                        ));
//                if (!existingPermissions.contains(permission)) {
//                    newPermissions.add(permission);
//                }
//            }
//        }
//        System.out.println("=======> 6");
//
//        if (!newPermissions.isEmpty()) {
//            existingPermissions.addAll(newPermissions);
//            roleAdmin.setPermissions(existingPermissions);
//            roleRepository.save(roleAdmin);
//        }
//        System.out.println("=======> 7");
//
//        // 3. Create admin user if not already present
//        if (userRepository.findByUsername("admin").isEmpty()) {
//            User user = new User();
//            user.setUsername("admin");
//            user.setPassword(passwordEncoder.encode("Admin@123"));
//            user.setActive(true);
//            user.setEmail("abc@gmail.com");
//            user.setRolez(Set.of(roleAdmin));
//            User savedUser = userRepository.save(user);
//
//            // 4. Setup default organization if needed
////            if (organizationLevelRepository.count() < 1) {
////                OrganizationLevel organizationLevel = new OrganizationLevel();
////                organizationLevel.setName("HEAD QUARTER");
////                organizationLevel.setDescription("HEAD QUARTER");
////                OrganizationLevel savedOrganizationLevel = organizationLevelRepository.save(organizationLevel);
////
////                OrganizationUnit organizationUnit = new OrganizationUnit();
////                organizationUnit.setName("HEAD QUARTER");
////                organizationUnit.setDescription("HEAD QUARTER");
////                organizationUnit.setOrganizationLevel(savedOrganizationLevel);
////                OrganizationUnit savedOrganizationUnit = organizationUnitRepository.save(organizationUnit);
////
////                savedUser.setCurrentLocation(savedOrganizationUnit);
////                userRepository.save(savedUser);
////            }
//        }
//    }
//
//}
