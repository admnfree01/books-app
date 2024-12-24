package com.base.booksapp.util;


import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "spva9ypgL0fr03QwNmGTIZZi4QkGAiMtgj+OV2joL5I=";

    // Token expiration time (10 hours)
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email) // Email as the subject
                .claim("roles", roles) // Add roles as a custom claim
                .setIssuedAt(new Date()) // Set the issue time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Set expiration time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Sign the token with the secret key
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List<String> extractRoles(String token) {
        return (List<String>) Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("roles");
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
