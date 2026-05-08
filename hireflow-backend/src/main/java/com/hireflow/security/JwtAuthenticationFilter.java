package com.hireflow.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected  boolean shouldNotFilter(HttpServletRequest request)
    {
        String path = request.getServletPath();
        return  path.startsWith("/api/v1/auth");
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        log.debug("JWT Filter invoked for URI: {}", request.getRequestURI());

        final String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            log.warn(
                    "Missing or invalid Authorization header for URI: {}",
                    request.getRequestURI()
            );

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        log.debug(
                "JWT token extracted successfully for URI: {}",
                request.getRequestURI()
        );

        try {

            log.debug("Validating JWT token...");

            String email = jwtUtil.extractEmail(token);

            if (email != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(email);

                log.info(
                        "User details loaded successfully for email: {}",
                        email
                );

                if (jwtUtil.isTokenValid(token)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);

                    log.info(
                            "Authentication successful for user: {}",
                            email
                    );

                } else {
                    log.error(
                            "JWT token validation failed for URI: {}",
                            request.getRequestURI()
                    );
                }
            }

        } catch (Exception e) {

            log.error(
                    "JWT authentication error for URI: {} | Error: {}",
                    request.getRequestURI(),
                    e.getMessage()
            );
        }

        filterChain.doFilter(request, response);
    }
}