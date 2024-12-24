package com.base.booksapp.controller;

import com.base.booksapp.dto.LoginRequest;
import com.base.booksapp.dto.LoginResponse;
import com.base.booksapp.dto.RegisterRequest;
import com.base.booksapp.model.User;
import com.base.booksapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
