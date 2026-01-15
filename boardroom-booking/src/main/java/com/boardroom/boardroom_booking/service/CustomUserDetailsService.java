package com.boardroom.boardroom_booking.service;

import com.boardroom.boardroom_booking.Configuration.security.CustomUserDetails;
import com.boardroom.boardroom_booking.model.User;
import com.boardroom.boardroom_booking.repository.UserRepository;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("In here again: user name: " + username);

        try {
            Optional<User> userOptional = userRepository.findByEmail(username);

            if (userOptional.isEmpty()) {
                System.out.println("User not found: " + username);
                throw new UsernameNotFoundException("User not found: " + username);
            }

            User user = userOptional.get();
            System.out.println("User found: " + user.getEmail());
            System.out.println("Password from DB: " + user.getPassword());
            //System.out.println("Roles: " + user.getRoles());

            return new CustomUserDetails(user);
        } catch (Exception e) {
            System.out.println("🔥 ERROR in UserDetailsService: " + e.getMessage());
            e.printStackTrace();  // ✅ Print full stack trace
            throw new InternalAuthenticationServiceException("Error during authentication: "+ e);
        }
    }

}
