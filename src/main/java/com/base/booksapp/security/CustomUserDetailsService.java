package com.base.booksapp.security;

import com.base.booksapp.model.User;
import com.base.booksapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Handle users with no password (e.g., Google OAuth2 users)
        String password = user.getPassword() != null ? user.getPassword() : ""; // Empty password for Google users

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(password) // Provide empty password for Google users
                .authorities(Collections.singletonList(() -> "ROLE_USER")) // Assign roles
                .build();
    }
}
