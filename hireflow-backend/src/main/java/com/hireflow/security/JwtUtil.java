package com.hireflow.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(
            String email,
            String role
    ) {

        log.info(
                "Generating JWT token for user: {} with role: {}",
                email,
                role
        );

        String token = Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis() + expiration
                        )
                )
                .signWith(getSigningKey())
                .compact();

        log.debug(
                "JWT token generated successfully for user: {}",
                email
        );

        return token;
    }

    public String extractEmail(String token) {

        log.debug("Extracting email from JWT token");

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String email = claims.getSubject();

        log.debug(
                "Email extracted successfully from token: {}",
                email
        );

        return email;
    }

    public boolean isTokenValid(String token) {

        try {

            log.debug("Validating JWT token");

            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            log.debug("JWT token validation successful");

            return true;

        } catch (JwtException e) {

            log.error(
                    "JWT validation failed: {}",
                    e.getMessage()
            );

            return false;
        }
    }
}