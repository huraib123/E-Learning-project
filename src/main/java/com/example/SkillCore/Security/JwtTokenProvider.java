package com.example.SkillCore.Security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;

@Component
public class JwtTokenProvider {

    private final SecretKey key;

    private final long jwtExpirationMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expirationMs}") long jwtExpirationMs) {
        // Initialize signing key from secret bytes (case sensitive)
    	  byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
          this.key = Keys.hmacShaKeyFor(keyBytes);
          this.jwtExpirationMs = jwtExpirationMs;
    }

    // Generate JWT token based on authenticated user details
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())    // username or email as subject
                .setIssuedAt(now)                            // issue time = now
                .setExpiration(expiryDate)                   // expiration time
                .signWith(key, SignatureAlgorithm.HS512)    // sign with HS512 algorithm & secret key
                .compact();
    }

    // Extract token from Authorization header (Bearer <token>)
    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Extract username (subject) from JWT token
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // Validate JWT token signature and expiration
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            // log exception if needed
            return false;
        }
    }
}
