package com.base.booksapp.controller;

import com.base.booksapp.dto.LoginRequest;
import com.base.booksapp.dto.LoginResponse;
import com.base.booksapp.dto.RegisterRequest;
import com.base.booksapp.model.User;
import com.base.booksapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request) {
        User registeredUser = authService.register(request);
        // Generate token for the registered user
        String token = authService.generateTokenForUser(registeredUser);
        return new LoginResponse(token);
    }

    @GetMapping("/google/success")
    public String googleAuthSuccess(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Check if the user already exists in the database
        User user = authService.registerOrUpdateGoogleUser(email, name);

        // Generate a JWT token for the authenticated user
        String token = authService.generateTokenForUser(user);

        return String.format("Google Auth Successful! JWT Token: %s", token);
    }
}
