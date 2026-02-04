package com.boardroom.boardroom_booking.serviceImpl;

import com.boardroom.boardroom_booking.Configuration.security.CurrentUserUtil;
import com.boardroom.boardroom_booking.audit.service.AuditService;
import com.boardroom.boardroom_booking.model.User;
import com.boardroom.boardroom_booking.repository.PermissionRepository;
import com.boardroom.boardroom_booking.repository.RoleRepository;
import com.boardroom.boardroom_booking.repository.UserRepository;
import com.boardroom.boardroom_booking.repository.UserRoleRepository;
import com.boardroom.boardroom_booking.service.UserService;
import com.boardroom.boardroom_booking.utils.GlobalMethod;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private  final GlobalMethod globalMethod;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final AuditService auditService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByDepartment(String department) {
        return userRepository.findByDepartmentName(department);
    }

    @Override
    public List<User> getUsersByDepartmentId(Long id) {
        return userRepository.findByDepartmentId(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found by id: " + id));

        existingUser.setFullName(user.getFullName());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setEmail(user.getEmail());
        existingUser.setBookings(user.getBookings());
        existingUser.setDepartment(user.getDepartment());

        return userRepository.save(existingUser);
    }

    @Override
    public UserDetails findByUsername(String name) throws UsernameNotFoundException {
        Optional<User> optUserAccount = userRepository.findByUsername(name);
        if (optUserAccount.isEmpty()) {
            throw new UsernameNotFoundException(name);
        }

        User currentUserOptional = optUserAccount.get();

        System.out.println("Get auth here ==========================>");

        Collection<? extends GrantedAuthority> authorities = getAuthorities(
                currentUserOptional);

        System.out.println("Get auth here ==========================> 2:");
        System.out.println("Get auth here ==========================> 2:"+authorities);

        currentUserOptional.setAuthorities(authorities);
        System.out.println("===========> authorities: "+currentUserOptional.getAuthorities());

        return currentUserOptional;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User userAccount) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        permissionRepository.getUserPermissions(userAccount.getId()).forEach(permission -> {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        });
        System.out.println("imeingia ===================>");
        return authorities;
    }

    @Override
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)){
            throw new RuntimeException("User not find by id: " + id);
        }
        userRepository.deleteById(id);
    }
}
