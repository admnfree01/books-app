package com.base.booksapp.service;

import com.base.booksapp.AuthProvider;
import com.base.booksapp.dto.RegisterRequest;
import com.base.booksapp.dto.LoginRequest;
import com.base.booksapp.dto.LoginResponse;
import com.base.booksapp.model.User;
import com.base.booksapp.repository.UserRepository;
import com.base.booksapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Handles user registration.
     *
     * @param request The registration request containing email, password, and name.
     * @return The registered user.
     */
    public User register(RegisterRequest request) {
        // Check if email is already registered
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        // Create and save a new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash the password
        user.setName(request.getName());
        user.setAuthProvider(AuthProvider.LOCAL); // Set provider to LOCAL for email/password registration

        return userRepository.save(user);
    }

    /**
     * Handles user login and generates a JWT token.
     *
     * @param request The login request containing email and password.
     * @return A login response containing the JWT token.
     */
    public LoginResponse login(LoginRequest request) {
        // Fetch user by email
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOpt.get();

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Generate JWT token
        String token = generateTokenForUser(user);

        return new LoginResponse(token);
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param user The user for whom the token is generated.
     * @return The generated JWT token.
     */
    public String generateTokenForUser(User user) {
        // Convert roles to a list of strings (if applicable)
        List<String> roles = List.of("USER"); // Assuming a single role for now
        return jwtUtil.generateToken(user.getEmail(), roles);
    }
}
